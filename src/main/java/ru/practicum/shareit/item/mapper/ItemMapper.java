package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;


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
                                              BookingWithoutItemDto nextBooking) {
        ItemWithBookingDto itemDto = new ItemWithBookingDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        return itemDto;
    }
}
