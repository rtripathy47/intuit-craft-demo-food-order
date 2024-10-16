package com.food.order.system.service.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.food.order.system.entity.Item;
import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderItemFulfillment;
import com.food.order.system.entity.Restaurant;
import com.food.order.system.entity.RestaurantItem;
import com.food.order.system.exception.InsufficientCapacityException;
import com.food.order.system.repository.RestaurantRepository;
import com.food.order.system.service.RedisService;
import com.food.order.system.service.stratergy.LowerCostStrategy;

@ExtendWith(MockitoExtension.class)
class LowerCostStrategyTest {

    @Mock
    private RedisService redisService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private LowerCostStrategy lowerCostStrategy;

    private OrderItem orderItem;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private RestaurantItem menuItem1;
    private RestaurantItem menuItem2;

    @BeforeEach
    void setUp() {
        orderItem = new OrderItem();
        orderItem.setQuantity(5);
        orderItem.setItem(new Item());

        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setMaxCapacity(10);
        restaurant1.setCurrentLoad(5);

        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setMaxCapacity(10);
        restaurant2.setCurrentLoad(5);

        menuItem1 = new RestaurantItem();
        menuItem1.setRestaurant(restaurant1);
        menuItem1.setPrice(10.0);

        menuItem2 = new RestaurantItem();
        menuItem2.setRestaurant(restaurant2);
        menuItem2.setPrice(8.0);
    }

    @Test
    void allocateOrderItem_shouldSortMenuItemsByPrice() {
        List<RestaurantItem> menuItems = Arrays.asList(menuItem1, menuItem2);

        lowerCostStrategy.allocateOrderItem(orderItem, menuItems);

        assertEquals(menuItem2, menuItems.get(0));
    }

    @Test
    void allocateOrderItem_shouldAllocateToMultipleRestaurantsIfNeeded() {
        List<RestaurantItem> menuItems = Arrays.asList(menuItem1, menuItem2);
        orderItem.setQuantity(6);
        List<OrderItemFulfillment> fulfillments = lowerCostStrategy.allocateOrderItem(orderItem, menuItems);

        assertEquals(2, fulfillments.size());
    }

    @Test
    void allocateOrderItem_shouldUpdateRestaurantCurrentLoad() {
        List<RestaurantItem> menuItems = Arrays.asList(menuItem2);

        lowerCostStrategy.allocateOrderItem(orderItem, menuItems);

        verify(restaurantRepository).save(argThat(restaurant -> 
            restaurant.getCurrentLoad() == 10
        ));
    }


    @Test
    void allocateOrderItem_shouldSetCorrectPricePerUnit() {
        List<RestaurantItem> menuItems = Arrays.asList(menuItem2);

        List<OrderItemFulfillment> fulfillments = lowerCostStrategy.allocateOrderItem(orderItem, menuItems);

        assertEquals(8.0, fulfillments.get(0).getPricePerUnit());
    }

    @Test
    void allocateOrderItem_shouldThrowExceptionWhenInsufficientCapacity() {
        restaurant2.setCurrentLoad(10);
        List<RestaurantItem> menuItems = Arrays.asList(menuItem2);

        assertThrows(InsufficientCapacityException.class, () -> 
            lowerCostStrategy.allocateOrderItem(orderItem, menuItems)
        );
    }

    @Test
    void allocateOrderItem_shouldSkipRestaurantsWithNoCapacity() {
        restaurant2.setCurrentLoad(10);
        List<RestaurantItem> menuItems = Arrays.asList(menuItem2, menuItem1);

        List<OrderItemFulfillment> fulfillments = lowerCostStrategy.allocateOrderItem(orderItem, menuItems);

        assertEquals(1, fulfillments.size());
        assertEquals(restaurant1, fulfillments.get(0).getRestaurant());
    }

    @Test
    void allocateOrderItem_shouldSetOrderItemInFulfillment() {
        List<RestaurantItem> menuItems = Arrays.asList(menuItem2);

        List<OrderItemFulfillment> fulfillments = lowerCostStrategy.allocateOrderItem(orderItem, menuItems);

        assertEquals(orderItem, fulfillments.get(0).getOrderItem());
    }
}
