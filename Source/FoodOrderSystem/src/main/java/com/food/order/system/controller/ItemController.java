package com.food.order.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.system.dto.ItemAvailabilityDto;
import com.food.order.system.dto.ItemDto;
import com.food.order.system.entity.Item;
import com.food.order.system.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public Item addItem(@Valid @RequestBody ItemDto itemDto) {
        return itemService.addItem(itemDto);
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/available")
    public ResponseEntity<Page<ItemAvailabilityDto>> getAvailableItems(Pageable pageable) {
        Page<ItemAvailabilityDto> itemsPage = itemService.getAvailableItems(pageable);
        return ResponseEntity.ok(itemsPage);
    }
}
