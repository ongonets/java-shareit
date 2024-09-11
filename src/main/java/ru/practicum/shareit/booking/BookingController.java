package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                              @RequestBody Booking booking) {
        log.info("Request to create booking  {}", booking);
        return bookingService.createBooking(bookerId, booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long bookingId,
                              @RequestParam Boolean approved) {
        log.info("Request to approve booking with ID = {}", bookingId);
        return bookingService.updateBooking(userId,bookingId,approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long bookingId) {
        log.info("Request to find booking with ID = {}", bookingId);
        return bookingService.findBooking(userId,bookingId);
    }

    @GetMapping
    public Collection<Booking> findAllBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Request to search bookings with user ID = {}", userId);
        return bookingService.findAllBookings(userId,state);
    }

    @GetMapping("/owner")
    public Collection<Booking> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Request to search bookings with owner ID = {}", userId);
        return bookingService.findOwnerBookings(userId,state);
    }
}
