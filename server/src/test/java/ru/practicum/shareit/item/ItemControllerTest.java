package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final ItemDto itemDto = new ItemDto(1L, "name", "description", true);
    private final ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L,
            "name",
            "description",
            true,
            new BookingWithoutItemDto(),
            new BookingWithoutItemDto(),
            new ArrayList<>()
    );
    private final CommentDto commentDto = new CommentDto(1L, "name", "text", Instant.now());
    private final NewItemRequest newItem = new NewItemRequest();
    private final NewCommentRequest newCommentRequest = new NewCommentRequest();


    @Test
    void findItemById() throws Exception {
        when(itemService.findItemById(1))
                .thenReturn(itemWithBookingDto);

        mvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithBookingDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithBookingDto.isAvailable())))
                .andExpect(jsonPath("$.lastBooking").value(itemWithBookingDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemWithBookingDto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemWithBookingDto.getComments()));
    }

    @Test
    void findOwnerItems() throws Exception {
        when(itemService.findOwnerItems(1))
                .thenReturn(Arrays.asList(itemWithBookingDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id","1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(itemWithBookingDto))));
    }

    @Test
    void itemSearch() throws Exception {
        when(itemService.findByText("test"))
                .thenReturn(Arrays.asList(itemDto));

        mvc.perform(get("/items/search?text={text}", "test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(itemDto))));
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(1, newItem))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.isAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(1,1, newItem))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}",1)
                        .content(mapper.writeValueAsString(newItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.isAvailable())));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/{itemId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, Mockito.times(1))
                .deleteItem(1);
    }

    @Test
    void createComment() throws Exception {
        when(itemService.createComment(1,1, newCommentRequest))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment",1)
                        .content(mapper.writeValueAsString(newCommentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }
}