package com.food.order.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.system.dto.OrderDto;
import com.food.order.system.dto.OrderResponse;
import com.food.order.system.dto.OrderStatusDto;
import com.food.order.system.service.AsyncOrderProcessor;
import com.food.order.system.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AsyncOrderProcessor asyncOrderProcessor;
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/place")
    public OrderResponse placeOrder(@Valid @RequestBody OrderDto orderDto) {
    	OrderResponse orderResponse= orderService.placeOrder(orderDto);
    	logger.info("Order successfully placed by customer : {} . Order id for future reference is {} ",orderDto.getCustomerId(),orderResponse.getOrderId());
    	   // Process the order asynchronously with retry
        asyncOrderProcessor.processOrder(orderResponse.getOrderId());
        return orderResponse;
    }

    @GetMapping("/{orderId}/status")
    public OrderStatusDto getOrderStatus(@PathVariable Long orderId) {
        return orderService.getOrderStatus(orderId);
    }
}
