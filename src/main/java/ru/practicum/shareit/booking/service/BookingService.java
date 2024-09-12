package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    BookingDto createBooking(long bookerId, NewBookingRequest bookingRequest);

    BookingDto updateBooking(long userId, long bookingId, Boolean approved);

    BookingDto findUserBooking(long userId, long bookingId);

    Collection<BookingDto> findBookerBookings(long userId, BookingState state);

    Collection<BookingDto> findOwnerBookings(long userId, BookingState state);
}
