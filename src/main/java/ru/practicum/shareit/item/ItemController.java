package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;


/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public Item findItemById(@PathVariable long itemId) {
        log.info("Request to search item with ID = {}", itemId);
        return itemService.findItemById(itemId);
    }

    @GetMapping
    public Collection<Item> findOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request to search item with owner ID = {}", userId);
        return itemService.findOwnerItems(userId);
    }

    @GetMapping("/search")
    public Collection<Item> itemSearch(@RequestParam String text) {
        log.info("Request to search item");
        return itemService.findByText(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody ItemDto item) {
        log.info("Request to create item  {}", item);
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId,
                           @RequestBody ItemDto item) {
        log.info("Request to update item with ID = {}", itemId);
        return itemService.updateItem(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable long itemId) {
        log.info("Request to delete item with ID = {}", itemId);
        itemService.deleteItem(itemId);
    }

}
