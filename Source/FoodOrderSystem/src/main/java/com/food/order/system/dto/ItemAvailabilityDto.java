package com.food.order.system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemAvailabilityDto {
    private Long itemId;
    private String itemName;
    private String description;
}
