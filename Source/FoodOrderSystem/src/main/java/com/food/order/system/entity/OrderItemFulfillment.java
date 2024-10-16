package com.food.order.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_fulfillments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemFulfillment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantityFulfilled;
    private Double pricePerUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
