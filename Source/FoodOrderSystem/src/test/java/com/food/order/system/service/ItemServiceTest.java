package com.food.order.system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.food.order.system.dto.ItemAvailabilityDto;
import com.food.order.system.dto.ItemDto;
import com.food.order.system.entity.Item;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.ItemRepository;
import com.food.order.system.repository.RestaurantItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RestaurantItemRepository restaurantItemRepository;

    @InjectMocks
    private ItemService itemService;

    private ItemDto itemDto;
    private Item item;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
    }

    @Test
    void addItem_shouldSaveItem() {
        when(itemRepository.findByName("Test Item")).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.addItem(itemDto);

        assertEquals(1L, result.getId());
    }

    @Test
    void addItem_shouldThrowExceptionWhenItemAlreadyExists() {
        when(itemRepository.findByName("Test Item")).thenReturn(Optional.of(new Item()));

        assertThrows(FoodOrderSystemException.class, () -> itemService.addItem(itemDto));
    }

    @Test
    void getAllItems_shouldReturnAllItems() {
        List<Item> items = Arrays.asList(item, new Item());
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.getAllItems();

        assertEquals(2, result.size());
    }

    @Test
    void getAvailableItems_shouldReturnPaginatedAvailableItems() {
        Page<Item> itemPage = new PageImpl<>(Arrays.asList(item));
        when(restaurantItemRepository.findDistinctAvailableItems(any(Pageable.class))).thenReturn(itemPage);

        Page<ItemAvailabilityDto> result = itemService.getAvailableItems(Pageable.unpaged());

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getAvailableItems_shouldMapItemsToItemAvailabilityDto() {
        Page<Item> itemPage = new PageImpl<>(Arrays.asList(item));
        when(restaurantItemRepository.findDistinctAvailableItems(any(Pageable.class))).thenReturn(itemPage);

        Page<ItemAvailabilityDto> result = itemService.getAvailableItems(Pageable.unpaged());

        ItemAvailabilityDto dto = result.getContent().get(0);
        assertEquals(1L, dto.getItemId());
        assertEquals("Test Item", dto.getItemName());
        assertEquals("Test Description", dto.getDescription());
    }
}
