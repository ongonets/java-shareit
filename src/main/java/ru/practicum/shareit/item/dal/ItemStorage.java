package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Collection<Item> findByText(String text);

    Optional<Item> findItemById(long itemId);

    Collection<Item> findOwnerItems(long ownerId);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(long itemId);
}
