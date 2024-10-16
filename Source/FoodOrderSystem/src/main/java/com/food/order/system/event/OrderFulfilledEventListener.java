package com.food.order.system.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.food.order.system.dto.DispatchNotification;
import com.food.order.system.entity.Order;
import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderItemFulfillment;
import com.food.order.system.service.NotificationService;
import com.food.order.system.service.RedisService;

@Component
public class OrderFulfilledEventListener {

	private static final Logger logger = LoggerFactory.getLogger(OrderFulfilledEventListener.class);

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private RedisService redisService;

	@Async
	@TransactionalEventListener
	public void handleOrderFulfilledEvent(OrderFulfilledEvent event) {
		Order order = event.getOrder();
		try {
			for (OrderItem orderItem : order.getOrderItems()) {
				for (OrderItemFulfillment fulfillment : orderItem.getFulfillments()) {
					DispatchNotification notification = new DispatchNotification(fulfillment.getRestaurant().getId(),
							orderItem.getId(), fulfillment.getQuantityFulfilled());
					/*
					 *  decrease processing capacity  of restaurant post transaction is committed is commentef as there can be temporary inconsistencies even though optimistic lock is present
					 */
//					redisService.decreaseCapacity(fulfillment.getRestaurant().getId(), fulfillment.getQuantityFulfilled());
					// Send dispatch notification asynchronously
					notificationService.handleDispatchNotification(notification);
				}
			}
			logger.info("Dispatch notifications sent for Order ID: {}", order.getId());
		} catch (Exception e) {
			logger.error("Failed to handle OrderFulfilledEvent for Order ID: {}", order.getId(), e);
			throw e; // Trigger retry mechanism
		}
	}

}
