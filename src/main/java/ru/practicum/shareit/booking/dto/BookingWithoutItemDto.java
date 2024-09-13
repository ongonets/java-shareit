package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingWithoutItemDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto booker;
    private BookingStatus status;
}
