package com.food.order.system.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Item name can only contain letters, numbers, and spaces")
    private String name;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
}
