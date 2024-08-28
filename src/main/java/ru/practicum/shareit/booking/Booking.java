package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long itemId;
    long bookerId;
    BookingStatus status;
}
