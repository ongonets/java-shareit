package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItem;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto crateItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody ItemRequestDto newRequest) {
        log.info("Request to create itemRequest  {}", newRequest);
        return itemRequestService.createItemRequest(userId, newRequest);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItem findItemRequestById(@PathVariable long requestId) {
        log.info("Request to search itemRequest with ID = {}", requestId);
        return itemRequestService.findItemRequestById(requestId);
    }

    @GetMapping
    public Collection<ItemRequestWithItem> findRequesterItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request to search itemRequests with requester ID = {}", userId);
        return itemRequestService.findRequesterItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAllItemRequests() {
        log.info("Request to search all itemRequests");
        return itemRequestService.findAllItemRequests();
    }
}
