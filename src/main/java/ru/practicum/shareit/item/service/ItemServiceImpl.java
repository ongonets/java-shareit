package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<Item> findByText(String text) {
        if(text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
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
    public Item createItem(long userId, ItemDto itemDto) {
        findUser(userId);
        validateItem(itemDto);
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(userId);
        return itemStorage.createItem(item);
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto itemDto) {
        Item oldItem = checkItemOwner(userId, itemId);
        if (itemDto.hasName()) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.hasDescription()) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if(itemDto.hasAvailable()) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
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
        if (item.getOwnerId() != userId) {
            log.error("User with ID = {} does not own item with ID = {}", userId, itemId);
            throw new ConditionsNotMetException(
                    String.format("User with ID = %d does not own item with ID = %d", userId,itemId));
        }
        return item;
    }

    private void validateItem(ItemDto item) {
        if (item.getName() == null || item.getName().isBlank()) {
            log.error("Name was entered incorrectly by item {}", item);
            throw new ValidationException("Name was entered incorrectly");
        }
        if (item.getDescription() == null) {
            log.error("Description was entered incorrectly by item {}", item);
            throw new ValidationException("Description was entered incorrectly");
        }
        if(item.getAvailable() == null) {
            log.error("Available was entered incorrectly by item {}", item);
            throw new ValidationException("Available was entered incorrectly");
        }
    }
}
