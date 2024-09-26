package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                    @RequestBody NewBookingRequestDto bookingRequest) {
        log.info("Request to create booking  {}", bookingRequest);
        return bookingClient.createBooking(bookerId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam Boolean approved) {
        log.info("Request to approve booking with ID = {}", bookingId);
        return bookingClient.updateBooking(userId,bookingId,approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId) {
        log.info("Request to find booking with ID = {}", bookingId);
        return bookingClient.findUserBooking(userId,bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Request to search bookings with user ID = {}", userId);
        return bookingClient.findBookerBookings(userId,state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Request to search bookings with owner ID = {}", userId);
        return bookingClient.findOwnerBookings(userId,state);
    }
}