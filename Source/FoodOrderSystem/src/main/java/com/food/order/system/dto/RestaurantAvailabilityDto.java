package com.food.order.system.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAvailabilityDto {
    private Long restaurantId;
    private String restaurantName;
    private Double price;
}
