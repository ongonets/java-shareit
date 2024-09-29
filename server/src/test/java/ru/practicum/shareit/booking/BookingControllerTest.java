package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final BookingDto bookingDto = new BookingDto(1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1),
            new ItemDto(),
            new UserDto(),
            BookingStatus.APPROVED);
     private  final NewBookingRequest newBooking = new NewBookingRequest();

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(1, newBooking))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(newBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()));
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.updateBooking(1,1,true))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}?approved={approved}",1,true)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()));
    }

    @Test
    void findBooking() throws Exception {
        when(bookingService.findUserBooking(1,1))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()));
    }

    @Test
    void findAllBookings() throws Exception {
        when(bookingService.findBookerBookings(1, BookingState.ALL))
                .thenReturn(Arrays.asList(bookingDto));

        mvc.perform(get("/bookings?state={state}", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(bookingDto))));
    }

    @Test
    void findOwnerBookings() throws Exception {
        when(bookingService.findOwnerBookings(1, BookingState.ALL))
                .thenReturn(Arrays.asList(bookingDto));

        mvc.perform(get("/bookings/owner?state={state}", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(bookingDto))));
    }
}