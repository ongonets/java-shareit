package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemStorageImpl implements ItemStorage {
    Map<Long, Item> items;

    public ItemStorageImpl() {
        items = new HashMap<>();
    }

    @Override
    public Collection<Item> findByText(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().contains(text) || item.getDescription().contains(text)))
                .filter(Item::isAvailable)
                .toList();
    }

    @Override
    public Optional<Item> findItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return Optional.of(items.get(itemId));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Item> findOwnerItems(long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        item.setId(getId());
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public void deleteItem(long itemId) {
        items.remove(itemId);
    }

    private long getId() {
        return items.keySet().stream()
                .max(Long::compareTo)
                .map(aLong -> aLong + 1)
                .orElse(1L);
    }
}
