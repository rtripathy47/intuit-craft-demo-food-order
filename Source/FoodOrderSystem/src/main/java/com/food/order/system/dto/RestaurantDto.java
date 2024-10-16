package com.food.order.system.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    @NotBlank(message = "Restaurant name is required")
    @Size(max = 100, message = "Restaurant name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Restaurant name can only contain letters, numbers, and spaces")
    private String name;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be between 0.0 and 5.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be between 0.0 and 5.0")
    private Double rating;
}
