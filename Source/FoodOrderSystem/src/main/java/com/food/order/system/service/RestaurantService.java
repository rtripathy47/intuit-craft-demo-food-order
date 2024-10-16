package com.food.order.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.order.system.dto.MenuItemDto;
import com.food.order.system.dto.RestaurantDto;
import com.food.order.system.entity.Item;
import com.food.order.system.entity.Restaurant;
import com.food.order.system.entity.RestaurantItem;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.ItemRepository;
import com.food.order.system.repository.RestaurantItemRepository;
import com.food.order.system.repository.RestaurantRepository;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RestaurantItemRepository restaurantItemRepository;

    @Autowired
    private RedisService redisService;

    @Transactional
    public Restaurant registerRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDto.getName());
        restaurant.setMaxCapacity(restaurantDto.getMaxCapacity());
        restaurant.setRating(restaurantDto.getRating());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Initialize capacity in Redis
        //set the current load from redis is commented as there can be temporary inconsistencies even though optimistic lock is present
//        redisService.setCapacity(savedRestaurant.getId(), 0);

        return savedRestaurant;
    }

    @Transactional
    public void updateMenu(Long restaurantId, MenuItemDto menuItemDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new FoodOrderSystemException("Restaurant not found"));

        Item item = itemRepository.findByName(menuItemDto.getItemName())
                .orElseGet(() -> {
                    Item newItem = new Item();
                    newItem.setName(menuItemDto.getItemName());
                    newItem.setDescription(menuItemDto.getDescription());
                    return itemRepository.save(newItem);
                });

        RestaurantItem menuItem = new RestaurantItem();
        menuItem.setItem(item);
        menuItem.setRestaurant(restaurant);
        menuItem.setPrice(menuItemDto.getPrice());
        restaurantItemRepository.save(menuItem);
    }
    
}
