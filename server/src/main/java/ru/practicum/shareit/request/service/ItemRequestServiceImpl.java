package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto newRequest) {
        User user = findUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(newRequest, user);
        return ItemRequestMapper.mapToDto(requestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestWithItem findItemRequestById(long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Not found itemRequest with ID = {}", requestId);
                    return new NotFoundException(String.format("Not found itemRequest with ID = %d", requestId));
                });
        return findItem(itemRequest);
    }

    @Override
    public Collection<ItemRequestWithItem> findRequesterItemRequests(long userId) {
        findUser(userId);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        return itemRequests.stream()
                .map(this::findItem)
                .toList();
    }

    @Override
    public Collection<ItemRequestDto> findAllItemRequests() {
        return requestRepository.findOrderByCreated().stream()
                .map(ItemRequestMapper::mapToDto)
                .toList();
    }

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Not found user with ID = {}", userId);
                    return new NotFoundException(String.format("Not found user with ID = %d", userId));
                });
    }

    private ItemRequestWithItem findItem(ItemRequest itemRequest) {
        List<ShortItemDto> items = itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                .map(ItemMapper::mapToShortItemDto)
                .toList();
        return ItemRequestMapper.mapToDto(itemRequest, items);
    }


}
