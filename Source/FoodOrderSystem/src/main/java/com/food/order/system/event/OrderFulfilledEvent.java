package com.food.order.system.event;


import org.springframework.context.ApplicationEvent;

import com.food.order.system.entity.Order;

import lombok.Getter;

@Getter
public class OrderFulfilledEvent extends ApplicationEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Order order;

    public OrderFulfilledEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}
