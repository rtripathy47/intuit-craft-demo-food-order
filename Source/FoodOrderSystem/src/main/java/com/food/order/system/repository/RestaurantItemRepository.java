package com.food.order.system.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.order.system.entity.Item;
import com.food.order.system.entity.RestaurantItem;
@Repository
public interface RestaurantItemRepository extends JpaRepository<RestaurantItem, Long> {
	@Query("SELECT mi FROM RestaurantItem mi JOIN FETCH mi.restaurant WHERE mi.item = :item")
	List<RestaurantItem> findByItemWithRestaurant(@Param("item") Item item);

	@Query("SELECT DISTINCT mi.item FROM RestaurantItem mi")
	Page<Item> findDistinctAvailableItems(Pageable pageable);

}
