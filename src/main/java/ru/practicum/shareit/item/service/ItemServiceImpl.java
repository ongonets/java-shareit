package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
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
    public Collection<ItemDto> findByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.findByText(text).stream()
                .map(ItemMapper::mapToDto)
                .toList();
    }

    @Override
    public ItemDto findItemById(long itemId) {
        return ItemMapper.mapToDto(findItem(itemId));
    }

    @Override
    public Collection<ItemDto> findOwnerItems(long ownerId) {
        findUser(ownerId);
        return itemStorage.findOwnerItems(ownerId).stream()
                .map(ItemMapper::mapToDto)
                .toList();
    }

    @Override
    public ItemDto createItem(long userId, NewItemRequest newItemRequest) {
        findUser(userId);
        validateItem(newItemRequest);
        Item item = ItemMapper.mapToItem(newItemRequest);
        item.setOwnerId(userId);
        return ItemMapper.mapToDto(itemStorage.createItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, NewItemRequest newItemRequest) {
        Item oldItem = checkItemOwner(userId, itemId);
        if (newItemRequest.hasName()) {
            oldItem.setName(newItemRequest.getName());
        }
        if (newItemRequest.hasDescription()) {
            oldItem.setDescription(newItemRequest.getDescription());
        }
        if (newItemRequest.hasAvailable()) {
            oldItem.setAvailable(newItemRequest.getAvailable());
        }
        return ItemMapper.mapToDto(itemStorage.updateItem(oldItem));
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
        Item item = findItem(itemId);
        if (item.getOwnerId() != userId) {
            log.error("User with ID = {} does not own item with ID = {}", userId, itemId);
            throw new ConditionsNotMetException(
                    String.format("User with ID = %d does not own item with ID = %d", userId, itemId));
        }
        return item;
    }

    private void validateItem(NewItemRequest item) {
        if (item.getName() == null || item.getName().isBlank()) {
            log.error("Name was entered incorrectly by item {}", item);
            throw new ValidationException("Name was entered incorrectly");
        }
        if (item.getDescription() == null) {
            log.error("Description was entered incorrectly by item {}", item);
            throw new ValidationException("Description was entered incorrectly");
        }
        if (item.getAvailable() == null) {
            log.error("Available was entered incorrectly by item {}", item);
            throw new ValidationException("Available was entered incorrectly");
        }
    }

    private Item findItem(long itemId) {
        return itemStorage.findItemById(itemId)
                .orElseThrow(() -> {
                    log.error("Not found item with ID = {}", itemId);
                    return new NotFoundException(String.format("Not found item with ID = %d", itemId));
                });
    }
}
