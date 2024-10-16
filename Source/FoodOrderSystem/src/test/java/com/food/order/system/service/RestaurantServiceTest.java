package com.food.order.system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.food.order.system.dto.MenuItemDto;
import com.food.order.system.dto.RestaurantDto;
import com.food.order.system.entity.Item;
import com.food.order.system.entity.Restaurant;
import com.food.order.system.entity.RestaurantItem;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.ItemRepository;
import com.food.order.system.repository.RestaurantItemRepository;
import com.food.order.system.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RestaurantItemRepository restaurantItemRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantDto restaurantDto;
    private MenuItemDto menuItemDto;

    @BeforeEach
    void setUp() {
        restaurantDto = new RestaurantDto();
        restaurantDto.setName("Test Restaurant");
        restaurantDto.setMaxCapacity(100);
        restaurantDto.setRating(4.5);

        menuItemDto = new MenuItemDto();
        menuItemDto.setItemName("Test Item");
        menuItemDto.setDescription("Test Description");
        menuItemDto.setPrice(10.0);
    }

    @Test
    void registerRestaurant_shouldSaveRestaurant() {
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant savedRestaurant = invocation.getArgument(0);
            savedRestaurant.setId(1L);
            return savedRestaurant;
        });

        Restaurant result = restaurantService.registerRestaurant(restaurantDto);

        assertEquals(1L, result.getId());
    }

    @Test
    void registerRestaurant_shouldSetCorrectFields() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(new Restaurant());

        restaurantService.registerRestaurant(restaurantDto);

        verify(restaurantRepository).save(argThat(restaurant ->
            restaurant.getName().equals("Test Restaurant") &&
            restaurant.getMaxCapacity() == 100 &&
            restaurant.getRating() == 4.5
        ));
    }

    @Test
    void updateMenu_shouldCreateNewItemIfNotExists() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new Restaurant()));
        when(itemRepository.findByName("Test Item")).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenReturn(new Item());

        restaurantService.updateMenu(1L, menuItemDto);

        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateMenu_shouldUseExistingItemIfExists() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new Restaurant()));
        when(itemRepository.findByName("Test Item")).thenReturn(Optional.of(new Item()));

        restaurantService.updateMenu(1L, menuItemDto);

        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void updateMenu_shouldSaveRestaurantItem() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new Restaurant()));
        when(itemRepository.findByName("Test Item")).thenReturn(Optional.of(new Item()));

        restaurantService.updateMenu(1L, menuItemDto);

        verify(restaurantItemRepository).save(any(RestaurantItem.class));
    }

    @Test
    void updateMenu_shouldThrowExceptionWhenRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FoodOrderSystemException.class, () -> restaurantService.updateMenu(1L, menuItemDto));
    }
}