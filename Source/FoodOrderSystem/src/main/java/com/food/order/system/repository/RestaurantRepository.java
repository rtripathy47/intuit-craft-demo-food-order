package com.food.order.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.order.system.entity.Restaurant;
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
