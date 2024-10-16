package com.food.order.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.order.system.entity.OrderItemFulfillment;
@Repository
public interface OrderItemFulfillmentRepository extends JpaRepository<OrderItemFulfillment, Long> {
}
