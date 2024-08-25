package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<Item> findByText(String text);

    Item findItemById(long itemId);

    Collection<Item> findOwnerItems(long ownerId);

    Item createItem(long userId, NewItemRequest newItemRequest);

    Item updateItem(long userId, long itemId, NewItemRequest newItemRequest);

    void deleteItem(long itemId);
}
