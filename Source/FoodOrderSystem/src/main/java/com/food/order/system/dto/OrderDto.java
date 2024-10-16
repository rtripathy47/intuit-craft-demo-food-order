package com.food.order.system.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemDto> orderItems;
    
    @NotEmpty(message = "Order items are required")
    @Valid
    private int customerId;
}
