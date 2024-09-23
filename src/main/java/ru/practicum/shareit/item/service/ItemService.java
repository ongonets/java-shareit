package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findByText(String text);

    ItemWithBookingDto findItemById(long itemId);

    Collection<ItemWithBookingDto> findOwnerItems(long ownerId);

    ItemDto createItem(long userId, NewItemRequest newItemRequest);

    ItemDto updateItem(long userId, long itemId, NewItemRequest newItemRequest);

    void deleteItem(long itemId);

    CommentDto createComment(long userId, long itemId, NewCommentRequest commentRequest);
}
