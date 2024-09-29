package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingDto {
    long id;
    String name;
    String description;
    boolean available;
    BookingWithoutItemDto lastBooking;
    BookingWithoutItemDto nextBooking;
    List<CommentDto> comments;
}