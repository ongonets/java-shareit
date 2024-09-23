package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@NoArgsConstructor
public final class ItemMapper {

    public static Item mapToItem(NewItemRequest newItemRequest) {
        Item item = new Item();
        item.setName(newItemRequest.getName());
        item.setDescription(newItemRequest.getDescription());
        item.setAvailable(newItemRequest.getAvailable());
        return item;
    }

    public static ItemDto mapToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        return itemDto;
    }

    public static ItemWithBookingDto mapToDto(Item item,
                                              BookingWithoutItemDto lastBooking,
                                              BookingWithoutItemDto nextBooking,
                                              List<CommentDto> comments) {
        ItemWithBookingDto itemDto = new ItemWithBookingDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(comments);
        return itemDto;
    }

    public static ShortItemDto mapToShortItemDto(Item item) {
        ShortItemDto itemDto = new ShortItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setOwnerId(item.getUser().getId());
        return itemDto;
    }
}
