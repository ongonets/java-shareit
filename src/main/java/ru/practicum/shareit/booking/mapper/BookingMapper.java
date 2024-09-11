package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking mapToBooking(BookingDto bookingDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingDto mapToDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }
}
