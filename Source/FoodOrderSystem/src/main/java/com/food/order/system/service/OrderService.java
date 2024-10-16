package com.food.order.system.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.order.system.dto.OrderDto;
import com.food.order.system.dto.OrderItemDto;
import com.food.order.system.dto.OrderResponse;
import com.food.order.system.dto.OrderStatusDto;
import com.food.order.system.entity.Item;
import com.food.order.system.entity.Order;
import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderStatus;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.ItemRepository;
import com.food.order.system.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;
  

    @Transactional
    //TODO if the order remains placed for a long period of time then it must be marked as failed
    public OrderResponse placeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderItems(new ArrayList<>());
        order.setStatus(OrderStatus.PLACED);
        order.setCustomerId(orderDto.getCustomerId());
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            Item item = itemRepository.findById(orderItemDto.getItemId())
                    .orElseThrow(() -> new FoodOrderSystemException("Item "+orderItemDto.getItemId() +" not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
        }

        // Save the order with status PLACED
        Order savedOrder = orderRepository.save(order);

        // Return immediate response with status PLACED
        return new OrderResponse(savedOrder.getId(), "Order has been placed.", savedOrder.getStatus().name());
    }

    public OrderStatusDto getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new FoodOrderSystemException("Order not found for order id "+orderId));
        return new OrderStatusDto(order.getId(), order.getStatus().name());
    }
}
