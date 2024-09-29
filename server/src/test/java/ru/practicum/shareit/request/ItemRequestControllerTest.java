package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItem;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private MockMvc mvc;

    private final ItemRequestDto requestDto = new ItemRequestDto(1L, "description", LocalDateTime.now());
    private final ItemRequestWithItem requestWithItemDto = new ItemRequestWithItem(1L,
            "description",
            LocalDateTime.now(),
            new ArrayList<>());
    private final ItemRequestDto newRequest = new ItemRequestDto();


    @Test
    void crateItemRequest() throws Exception {
        when(requestService.createItemRequest(1, newRequest))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(newRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",is(requestDto.getDescription())));
    }

    @Test
    void findItemRequestById() throws Exception {
        when(requestService.findItemRequestById(1))
                .thenReturn(requestWithItemDto);

        mvc.perform(get("/requests/{requestId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",is(requestDto.getDescription())));
    }

    @Test
    void findRequesterItemRequests() throws Exception {
        when(requestService.findRequesterItemRequests(1))
                .thenReturn(Arrays.asList(requestWithItemDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(requestWithItemDto))));
    }

    @Test
    void findAllItemRequests() throws Exception {
        when(requestService.findAllItemRequests())
                .thenReturn(Arrays.asList(requestDto));

                mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(requestDto))));
    }
}