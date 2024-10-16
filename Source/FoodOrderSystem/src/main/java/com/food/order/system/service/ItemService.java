package com.food.order.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.food.order.system.dto.ItemAvailabilityDto;
import com.food.order.system.dto.ItemDto;
import com.food.order.system.entity.Item;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.ItemRepository;
import com.food.order.system.repository.RestaurantItemRepository;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RestaurantItemRepository restaurantItemRepository;

    public Item addItem(ItemDto itemDto) {
        itemRepository.findByName(itemDto.getName()).ifPresent(item -> {
            throw new FoodOrderSystemException("Item with this name already exists");
        });

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Page<ItemAvailabilityDto> getAvailableItems(Pageable pageable) {
        // Fetch distinct items with positive quantity in a paginated way
        Page<Item> availableItems = restaurantItemRepository.findDistinctAvailableItems(pageable);

        // Map to ItemAvailabilityDto
        return availableItems.map(item -> new ItemAvailabilityDto(
                item.getId(),
                item.getName(),
                item.getDescription()
        ));
    }
}
