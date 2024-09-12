package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public BookingDto createBooking(long bookerId, BookingDto bookingDto) {
        User user = findUser(bookerId);
        Item item = findItem(bookingDto.getItemId());
        if (!item.isAvailable()) {
            log.error("Item is not available {}", item);
            throw new ValidationException("Item is not available.");
        }
        validateBookingDate(bookingDto);
        Booking booking = BookingMapper.mapToBooking(bookingDto, item, user);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.mapToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBooking(long userId, long bookingId, Boolean approved) {
        User user = findUser(userId);
        Booking booking = findBooking(bookingId);
        Item item = booking.getItem();
        if (item.getUser() != user) {
            log.error("User is not owner of item {}", item);
            throw new ConditionsNotMetException("User is not owner.");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.mapToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findUserBooking(long userId, long bookingId) {
        User user = findUser(userId);
        Booking booking = findBooking(bookingId);
        Item item = booking.getItem();
        if (item.getUser() != user && booking.getBooker() != user) {
            log.error("User is not owner or booker of item {}", item);
            throw new ConditionsNotMetException("User is not owner or booker.");
        }
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public Collection<BookingDto> findBookerBookings(long userId, BookingState state) {
        User user = findUser(userId);
        BooleanExpression byBooker = QBooking.booking.booker.eq(user);
        return findBookings(byBooker, state);
    }

    @Override
    public Collection<BookingDto> findOwnerBookings(long userId, BookingState state) {
        User user = findUser(userId);
        BooleanExpression byBooker = QBooking.booking.item.user.eq(user);
        return findBookings(byBooker, state);
    }

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Not found user with ID = {}", userId);
                    return new NotFoundException(String.format("Not found user with ID = %d", userId));
                });
    }

    private Item findItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Not found item with ID = {}", itemId);
                    return new NotFoundException(String.format("Not found item with ID = %d", itemId));
                });
    }

    private Booking findBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Not found booking with ID = {}", bookingId);
                    return new NotFoundException(String.format("Not found booking with ID = %d", bookingId));
                });
    }

    private void validateBookingDate(BookingDto bookingDto) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        LocalDateTime now = LocalDateTime.now();
        if (start == null || start.isBefore(now)) {
            log.error("Start date was entered incorrectly by booking {}", bookingDto);
            throw new ValidationException("Start date was entered incorrectly.");
        }
        if (end == null || end.isBefore(now)) {
            log.error("End date was entered incorrectly by booking {}", bookingDto);
            throw new ValidationException("End date was entered incorrectly.");
        }
        if (start.isEqual(end) || start.isAfter(end)) {
            log.error("Dates  entered incorrectly by booking {}", bookingDto);
            throw new ValidationException("Dates entered incorrectly.");
        }
    }

    private Collection<BookingDto> findBookings (BooleanExpression byBooker, BookingState state) {
        Iterable<Booking> foundBooking = new ArrayList<>();
        BooleanExpression byStatus;
        BooleanExpression byDate;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL -> foundBooking = bookingRepository.findAll(byBooker);
            case PAST -> {
                byDate = QBooking.booking.end.before(now);
                foundBooking = bookingRepository.findAll(byBooker.and(byDate));
            }
            case FUTURE -> {
                byDate = QBooking.booking.start.after(now);
                foundBooking = bookingRepository.findAll(byBooker.and(byDate));
            }
            case CURRENT -> {
                byDate = QBooking.booking.start.before(now).and(QBooking.booking.end.after(now));
                foundBooking = bookingRepository.findAll(byBooker.and(byDate));
            }
            case WAITING -> {
                byStatus = QBooking.booking.status.eq(BookingStatus.WAITING);
                foundBooking = bookingRepository.findAll(byBooker.and(byStatus));
            }
            case REJECTED -> {
                byStatus = QBooking.booking.status.eq(BookingStatus.REJECTED);
                foundBooking = bookingRepository.findAll(byBooker.and(byStatus));
            }
        }
        return StreamSupport.stream(foundBooking.spliterator(), false)
                .map(BookingMapper::mapToDto).toList();
    }
}
