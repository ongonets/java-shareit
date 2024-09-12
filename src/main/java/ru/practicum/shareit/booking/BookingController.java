package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Request to create booking  {}", bookingDto);
        return bookingService.createBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam Boolean approved) {
        log.info("Request to approve booking with ID = {}", bookingId);
        return bookingService.updateBooking(userId,bookingId,approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId) {
        log.info("Request to find booking with ID = {}", bookingId);
        return bookingService.findUserBooking(userId,bookingId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Request to search bookings with user ID = {}", userId);
        return bookingService.findBookerBookings(userId,state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Request to search bookings with owner ID = {}", userId);
        return bookingService.findOwnerBookings(userId,state);
    }
}
