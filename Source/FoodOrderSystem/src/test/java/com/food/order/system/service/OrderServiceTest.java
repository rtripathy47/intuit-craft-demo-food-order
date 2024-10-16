package com.food.order.system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.food.order.system.dto.OrderDto;
import com.food.order.system.dto.OrderItemDto;
import com.food.order.system.dto.OrderResponse;
import com.food.order.system.dto.OrderStatusDto;
import com.food.order.system.entity.Item;
import com.food.order.system.entity.Order;
import com.food.order.system.entity.OrderStatus;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.ItemRepository;
import com.food.order.system.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        OrderItemDto orderItemDto1 = new OrderItemDto();
        orderItemDto1.setItemId(1L);
        orderItemDto1.setQuantity(2);

        OrderItemDto orderItemDto2 = new OrderItemDto();
        orderItemDto2.setItemId(2L);
        orderItemDto2.setQuantity(1);

        orderDto = new OrderDto();
        orderDto.setCustomerId(1);
        orderDto.setOrderItems(Arrays.asList(orderItemDto1, orderItemDto2));
    }

    @Test
    void placeOrder_shouldCreateOrderSuccessfully() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });

        OrderResponse response = orderService.placeOrder(orderDto);

        assertEquals(1L, response.getOrderId());
    }

    @Test
    void placeOrder_shouldSetOrderStatusToPlaced() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.placeOrder(orderDto);

        assertEquals(OrderStatus.PLACED.name(), response.getStatus());
    }

    @Test
    void placeOrder_shouldThrowExceptionWhenItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(FoodOrderSystemException.class, () -> orderService.placeOrder(orderDto));
    }

    @Test
    void placeOrder_shouldSaveOrderWithCorrectItems() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.placeOrder(orderDto);

        verify(orderRepository).save(argThat(order -> 
            order.getOrderItems().size() == 2 &&
            order.getOrderItems().get(0).getItem().getId().equals(1L) &&
            order.getOrderItems().get(1).getItem().getId().equals(2L)
        ));
    }

    @Test
    void getOrderStatus_shouldReturnCorrectStatus() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PLACED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderStatusDto statusDto = orderService.getOrderStatus(1L);

        assertEquals(OrderStatus.PLACED.name(), statusDto.getStatus());
    }

    @Test
    void getOrderStatus_shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FoodOrderSystemException.class, () -> orderService.getOrderStatus(1L));
    }

    @Test
    void placeOrder_shouldSetCustomerId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.placeOrder(orderDto);

        verify(orderRepository).save(argThat(order -> 
            order.getCustomerId()==1
        ));
    }

    @Test
    void placeOrder_shouldSetCorrectQuantities() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.placeOrder(orderDto);

        verify(orderRepository).save(argThat(order -> 
            order.getOrderItems().get(0).getQuantity() == 2 &&
            order.getOrderItems().get(1).getQuantity() == 1
        ));
    }
}
