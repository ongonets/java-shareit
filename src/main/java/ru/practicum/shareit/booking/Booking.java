package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long itemId;
    long bookerId;
    BookingStatus status;
}
