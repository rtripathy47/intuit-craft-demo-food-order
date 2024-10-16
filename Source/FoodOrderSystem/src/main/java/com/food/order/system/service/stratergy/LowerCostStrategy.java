package com.food.order.system.service.stratergy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.food.order.system.entity.OrderItem;
import com.food.order.system.entity.OrderItemFulfillment;
import com.food.order.system.entity.Restaurant;
import com.food.order.system.entity.RestaurantItem;
import com.food.order.system.exception.InsufficientCapacityException;
import com.food.order.system.repository.RestaurantRepository;
import com.food.order.system.service.RedisService;

@Component("lowerCostStrategy")
public class LowerCostStrategy implements RestaurantSelectionStrategy {
	@Autowired
	private RedisService redisService;

	@Autowired
	private RestaurantRepository restaurantRepository;
	
	  
    private static final Logger logger = LoggerFactory.getLogger(LowerCostStrategy.class);

	@Override
	public List<OrderItemFulfillment> allocateOrderItem(final OrderItem orderItem,final List<RestaurantItem> menuItems) {
		// this is the low cost strategy which sorts the selected restaurants that are
		// serving the item in ascending order
		menuItems.sort(Comparator.comparingDouble(RestaurantItem::getPrice));
		int remainingQuantity = orderItem.getQuantity();
		List<OrderItemFulfillment> fulfillments = new ArrayList<>();

		for (RestaurantItem menuItem : menuItems) {
			Restaurant restaurant = menuItem.getRestaurant();
			// fetch the current load from redis is commented as there can be temporary inconsistencies even though optimistic lock is present
//			int availableCapacity = redisService.getCapacity(restaurant.getId());
			
			int availableCapacity = restaurant.getMaxCapacity()-restaurant.getCurrentLoad();

			if (availableCapacity <= 0) {
				continue;
			}

			int quantityToAllocate = Math.min(remainingQuantity, availableCapacity);
			restaurant.setCurrentLoad(restaurant.getCurrentLoad()+quantityToAllocate);
			
			// Save updates (will increment the version field)
			restaurantRepository.save(restaurant);


			OrderItemFulfillment fulfillment = new OrderItemFulfillment();
			fulfillment.setOrderItem(orderItem);
			fulfillment.setRestaurant(restaurant);
			fulfillment.setQuantityFulfilled(quantityToAllocate);
			fulfillment.setPricePerUnit(menuItem.getPrice());
			fulfillments.add(fulfillment);

			remainingQuantity -= quantityToAllocate;

			if (remainingQuantity == 0) {
				break;
			}
		}

		if (remainingQuantity > 0) {
			throw new InsufficientCapacityException(
					"Cannot fulfill order quantity for " + orderItem.getItem().getName() + " at the moment");
		}

		return fulfillments;
	}
}
