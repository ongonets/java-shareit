package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking mapToBooking(NewBookingRequest bookingDto, Item item, User booker) {
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
        ItemDto itemDto = ItemMapper.mapToDto(booking.getItem());
        bookingDto.setItem(itemDto);
        UserDto userDto = UserMapper.mapToDto(booking.getBooker());
        bookingDto.setBooker(userDto);
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static BookingWithoutItemDto mapToDtoWithoutItem(Booking booking) {
        BookingWithoutItemDto bookingDto = new BookingWithoutItemDto();
        bookingDto.setId(booking.getId());
        UserDto userDto = UserMapper.mapToDto(booking.getBooker());
        bookingDto.setBooker(userDto);
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }
}
