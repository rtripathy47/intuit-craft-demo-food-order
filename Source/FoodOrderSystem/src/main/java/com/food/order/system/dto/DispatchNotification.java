package com.food.order.system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DispatchNotification {
    private Long restaurantId;
    private Long orderItemId;
    private Integer quantityDispatched;
}
