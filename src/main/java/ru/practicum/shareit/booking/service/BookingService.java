package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    BookingDto createBooking(long bookerId, BookingDto bookingDto);

    BookingDto updateBooking(long userId, long bookingId, Boolean approved);

    BookingDto findBooking(long userId, long bookingId);

    Collection<BookingDto> findAllBookings(long userId, BookingState state);

    Collection<BookingDto> findOwnerBookings(long userId, BookingState state);
}
