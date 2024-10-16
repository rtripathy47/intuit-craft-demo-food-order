package com.food.order.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.system.dto.MenuItemDto;
import com.food.order.system.dto.RestaurantDto;
import com.food.order.system.entity.Restaurant;
import com.food.order.system.service.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

//    @PreAuthorize("hasRole('RESTAURANT')")
    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant registerRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        return restaurantService.registerRestaurant(restaurantDto);
    }

    @PutMapping(value="/{restaurantId}/menu", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateMenu(@PathVariable Long restaurantId, @Valid @RequestBody MenuItemDto menuItemDto) {
        restaurantService.updateMenu(restaurantId, menuItemDto);
    }
}
