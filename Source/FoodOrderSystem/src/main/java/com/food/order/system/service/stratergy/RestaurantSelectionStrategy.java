package com.food.order.system.service.stratergy;

import java.util.List;

import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderItemFulfillment;
import com.food.order.system.entity.RestaurantItem;

public interface RestaurantSelectionStrategy {
	List<OrderItemFulfillment> allocateOrderItem(final OrderItem orderItem,final List<RestaurantItem> menuItems);
}
