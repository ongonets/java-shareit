package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemWithBookingDto {
    long id;
    String name;
    String description;
    boolean available;
    BookingWithoutItemDto lastBooking;
    BookingWithoutItemDto nextBooking;
    List<CommentDto> comments;
}