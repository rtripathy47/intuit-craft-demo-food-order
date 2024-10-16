package com.food.order.system.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.OptimisticLockingFailureException;

import com.food.order.system.entity.Item;
import com.food.order.system.entity.Order;
import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderItemFulfillment;
import com.food.order.system.entity.OrderStatus;
import com.food.order.system.entity.RestaurantItem;
import com.food.order.system.event.OrderFulfilledEvent;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.OrderRepository;
import com.food.order.system.repository.RestaurantItemRepository;
import com.food.order.system.service.stratergy.RestaurantSelectionStrategy;

@ExtendWith(MockitoExtension.class)
class AsyncOrderProcessorTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantItemRepository restaurantItemRepository;

    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AsyncOrderProcessor asyncOrderProcessor;

    private Order order;
    private OrderItem orderItem;
    private Item item;
    private RestaurantItem restaurantItem;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        item = new Item();
        item.setName("Test Item");
        orderItem = new OrderItem();
        orderItem.setItem(item);
        order.setOrderItems(Collections.singletonList(orderItem));
        restaurantItem = new RestaurantItem();
    }

    @Test
    void processOrder_shouldFulfillOrderSuccessfully() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(restaurantItemRepository.findByItemWithRestaurant(item)).thenReturn(Collections.singletonList(restaurantItem));
        when(restaurantSelectionStrategy.allocateOrderItem(orderItem, Collections.singletonList(restaurantItem)))
            .thenReturn(Collections.singletonList(new OrderItemFulfillment()));

        asyncOrderProcessor.processOrder(1L);

        verify(orderRepository).save(argThat(savedOrder -> 
            savedOrder.getStatus() == OrderStatus.FULFILLED
        ));
    }

    @Test
    void processOrder_shouldPublishEventAfterFulfillment() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(restaurantItemRepository.findByItemWithRestaurant(item)).thenReturn(Collections.singletonList(restaurantItem));
        when(restaurantSelectionStrategy.allocateOrderItem(orderItem, Collections.singletonList(restaurantItem)))
            .thenReturn(Collections.singletonList(new OrderItemFulfillment()));

        asyncOrderProcessor.processOrder(1L);

        verify(eventPublisher).publishEvent(any(OrderFulfilledEvent.class));
    }

    @Test
    void processOrder_shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FoodOrderSystemException.class, () -> asyncOrderProcessor.processOrder(1L));
    }

    @Test
    void processOrder_shouldThrowExceptionWhenNoRestaurantsServeItem() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(restaurantItemRepository.findByItemWithRestaurant(item)).thenReturn(Collections.emptyList());

        assertThrows(FoodOrderSystemException.class, () -> asyncOrderProcessor.processOrder(1L));
    }

    @Test
    void processOrder_shouldSetOrderStatusToFailedOnException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(restaurantItemRepository.findByItemWithRestaurant(item)).thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> asyncOrderProcessor.processOrder(1L));

        verify(orderRepository).save(argThat(savedOrder -> 
            savedOrder.getStatus() == OrderStatus.FAILED
        ));
    }

    @Test
    void processOrder_shouldRetryOnOptimisticLockException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(restaurantItemRepository.findByItemWithRestaurant(item)).thenReturn(Collections.singletonList(restaurantItem));
        when(restaurantSelectionStrategy.allocateOrderItem(orderItem, Collections.singletonList(restaurantItem)))
            .thenThrow(OptimisticLockingFailureException.class)
            .thenReturn(Collections.singletonList(new OrderItemFulfillment()));
        assertThrows(OptimisticLockingFailureException.class, () -> 
        asyncOrderProcessor.processOrder(1L)
    );
    }

    @Test
    void recover_shouldSetOrderStatusToFailedAfterExhaustedRetries() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        asyncOrderProcessor.recover(new RuntimeException("Test exception"), 1L);

        verify(orderRepository).save(argThat(savedOrder -> 
            savedOrder.getStatus() == OrderStatus.FAILED
        ));
    }

    @Test
    void recover_shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FoodOrderSystemException.class, () -> 
            asyncOrderProcessor.recover(new RuntimeException("Test exception"), 1L)
        );
    }
}
