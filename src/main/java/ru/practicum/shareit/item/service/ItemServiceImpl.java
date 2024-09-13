package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Collection<ItemDto> findByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByText(text).stream()
                .map(ItemMapper::mapToDto)
                .toList();
    }

    @Override
    public ItemDto findItemById(long itemId) {
        return ItemMapper.mapToDto(findItem(itemId));
    }

    @Override
    public Collection<ItemWithBookingDto> findOwnerItems(long ownerId) {
        findUser(ownerId);
        return itemRepository.findAllByUserId(ownerId).stream()
                .map(this::findLastANdNextBooking)
                .toList();
    }

    @Override
    @Transactional
    public ItemDto createItem(long userId, NewItemRequest newItemRequest) {
        User user = findUser(userId);
        validateItem(newItemRequest);
        Item item = ItemMapper.mapToItem(newItemRequest);
        item.setUser(user);
        return ItemMapper.mapToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
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
        return ItemMapper.mapToDto(itemRepository.save(oldItem));
    }

    @Override
    @Transactional
    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Not found user with ID = {}", userId);
                    return new NotFoundException(String.format("Not found user with ID = %d", userId));
                });
    }

    private Item checkItemOwner(long userId, long itemId) {
        User user = findUser(userId);
        Item item = findItem(itemId);
        if (item.getUser() != user) {
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
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Not found item with ID = {}", itemId);
                    return new NotFoundException(String.format("Not found item with ID = %d", itemId));
                });
    }

    private ItemWithBookingDto findLastANdNextBooking(Item item) {
        BookingWithoutItemDto lastBooking = bookingRepository.findLastBookings(item, LocalDateTime.now())
                .map(BookingMapper::mapToDtoWithoutItem)
                .orElseGet(() -> null);
        BookingWithoutItemDto nextBooking = bookingRepository.findNextBookings(item, LocalDateTime.now())
                .map(BookingMapper::mapToDtoWithoutItem)
                .orElseGet(() -> null);
        return ItemMapper.mapToDto(item, lastBooking, nextBooking);
    }
}
