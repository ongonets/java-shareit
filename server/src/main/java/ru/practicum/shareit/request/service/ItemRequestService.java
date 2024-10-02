package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItem;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(long userId, ItemRequestDto newRequest);

    ItemRequestWithItem findItemRequestById(long requestId);

    Collection<ItemRequestWithItem> findRequesterItemRequests(long userId);

    Collection<ItemRequestDto> findAllItemRequests();
}
