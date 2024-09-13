package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.dto.NewItemRequest;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findByText(String text);

    ItemDto findItemById(long itemId);

    Collection<ItemWithBookingDto> findOwnerItems(long ownerId);

    ItemDto createItem(long userId, NewItemRequest newItemRequest);

    ItemDto updateItem(long userId, long itemId, NewItemRequest newItemRequest);

    void deleteItem(long itemId);
}
