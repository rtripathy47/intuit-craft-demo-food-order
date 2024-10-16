package com.food.order.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
