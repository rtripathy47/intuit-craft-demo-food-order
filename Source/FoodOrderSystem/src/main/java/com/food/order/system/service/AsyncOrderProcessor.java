
package com.food.order.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.order.system.entity.Item;
import com.food.order.system.entity.Order;
import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderItemFulfillment;
import com.food.order.system.entity.OrderStatus;
import com.food.order.system.entity.RestaurantItem;
import com.food.order.system.event.OrderFulfilledEvent;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.exception.InsufficientCapacityException;
import com.food.order.system.repository.OrderRepository;
import com.food.order.system.repository.RestaurantItemRepository;
import com.food.order.system.service.stratergy.RestaurantSelectionStrategy;

import jakarta.persistence.OptimisticLockException;

@Service
public class AsyncOrderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AsyncOrderProcessor.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantItemRepository restaurantItemRepository;

    @Autowired
    private RestaurantSelectionStrategy restaurantSelectionStrategy;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Async
    @Retryable(
        retryFor = { InsufficientCapacityException.class, OptimisticLockException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 20000)
    )
    @Transactional
    public void processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new FoodOrderSystemException("Order not found for ID: " + orderId));

        try {
            for (OrderItem orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem();

                // Fetch Restaurants that serve the Item
                final List<RestaurantItem> menuItems = restaurantItemRepository.findByItemWithRestaurant(item);

                if (menuItems.isEmpty()) {
                    throw new FoodOrderSystemException("No restaurants serve the item: " + item.getName());
                }

                // Apply strategy to select restaurants serving the item
                final List<OrderItemFulfillment> fulfillments = restaurantSelectionStrategy.allocateOrderItem(orderItem, menuItems);

                // Associate fulfillments with the order item
                orderItem.setFulfillments(fulfillments);
            }

            // Update order status to FULFILLED
            order.setStatus(OrderStatus.FULFILLED);
            orderRepository.save(order);

            // Publish OrderFulfilledEvent after successful transaction
            eventPublisher.publishEvent(new OrderFulfilledEvent(this, order));

            logger.info("Order ID: {} has been fulfilled and event published.", orderId);

        } catch (Exception e) {
            // Update order status to FAILED
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            logger.error("Order processing failed for Order ID: {}. Will Retry after 20s", orderId, e);
            throw e; // Rethrow to trigger retry mechanism
        }
    }

    @Recover
    public void recover(Exception e, Long orderId) {
        // Handle recovery logic after retries are exhausted
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new FoodOrderSystemException("Order not found with ID: " + orderId));

        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        // Optionally, notify the user or admin about the failure
        logger.error("Order processing failed after retries for Order ID: {}", orderId, e);
    }
}

