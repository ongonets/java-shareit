package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<Item> findByText(String text) {
        return itemStorage.findByText(text);
    }

    @Override
    public Item findItemById(long itemId) {
        return itemStorage.findItemById(itemId)
                .orElseThrow(() -> {
                    log.error("Not found item with ID = {}", itemId);
                    return new NotFoundException(String.format("Not found item with ID = %d", itemId));
                });
    }

    @Override
    public Collection<Item> findOwnerItems(long ownerId) {
        findUser(ownerId);
        return itemStorage.findOwnerItems(ownerId);
    }

    @Override
    public Item createItem(long userId, Item item) {
        findUser(userId);
        item.setOwnerId(userId);
        return itemStorage.createItem(item);
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        Item oldItem = checkItemOwner(userId, itemId);
        oldItem.setName(item.getName());
        oldItem.setDescription(item.getDescription());
        oldItem.setAvailable(item.isAvailable());
        return itemStorage.updateItem(oldItem);
    }

    @Override
    public void deleteItem(long itemId) {
        itemStorage.deleteItem(itemId);
    }

    private void findUser(long userId) {
        userStorage.findUserById(userId)
                .orElseThrow(() -> {
                    log.error("Not found user with ID = {}", userId);
                    return new NotFoundException(String.format("Not found user with ID = %d", userId));
                });
    }

    private Item checkItemOwner(long userId, long itemId) {
        findUser(userId);
        Item item = findItemById(itemId);
        if (item.getOwnerId() == userId) {
            log.error("User with ID = {} does not own item with ID = {}", userId, itemId);
            throw new ConditionsNotMetException(
                    String.format("User with ID = %d does not own item with ID = %d", userId,itemId));
        }
        return item;
    }
}
