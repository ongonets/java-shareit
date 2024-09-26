package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentRequestDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;

import java.util.Collection;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable long itemId) {
        log.info("Request to search item with ID = {}", itemId);
        return itemClient.findItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request to search item with owner ID = {}", userId);
        return itemClient.findOwnerItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> itemSearch(@RequestParam String text) {
        log.info("Request to search item");
        return itemClient.findByText(text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody NewItemRequestDto item) {
        log.info("Request to create item  {}", item);
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId,
                              @RequestBody NewItemRequestDto item) {
        log.info("Request to update item with ID = {}", itemId);
        return itemClient.updateItem(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable long itemId) {
        log.info("Request to delete item with ID = {}", itemId);
        return itemClient.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId,
                                    @RequestBody NewCommentRequestDto commentRequest) {
        log.info("Request to create comment  {}", commentRequest);
        return itemClient.createComment(userId, itemId, commentRequest);
    }
}
