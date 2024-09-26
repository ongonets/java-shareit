package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> crateItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestBody ItemRequestDto newRequest) {
        log.info("Request to create itemRequest  {}", newRequest);
        return requestClient.createItemRequest(userId, newRequest);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@PathVariable long requestId) {
        log.info("Request to search itemRequest with ID = {}", requestId);
        return requestClient.findItemRequestById(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> findRequesterItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request to search itemRequests with requester ID = {}", userId);
        return requestClient.findRequesterItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequests() {
        log.info("Request to search all itemRequests");
        return requestClient.findAllItemRequests();
    }
}
