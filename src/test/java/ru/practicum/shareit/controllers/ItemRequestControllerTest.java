package ru.practicum.shareit.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;
    User user = User.builder()
            .id(1L)
            .name("Anton")
            .email("mail@mail.ru")
            .build();
    ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("STRING")
            .requester(user)
            .created(LocalDateTime.now())
            .items(null)
            .build();

    ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("STRING")
            .requester(user)
            .created(LocalDateTime.now())
            .build();

    List<ItemRequestDto> listOfItemRequestDtos = new ArrayList<>();

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(any(), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(itemRequestDto);


        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requester.id", is(itemRequestDto.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.requester.name", is(itemRequestDto.getRequester().getName())))
                .andExpect(jsonPath("$.requester.email", is(itemRequestDto.getRequester().getEmail())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
        ;

    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getItemRequestById(any(Long.class), any(Long.class)))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requester.id", is(itemRequestDto.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.requester.name", is(itemRequestDto.getRequester().getName())))
                .andExpect(jsonPath("$.requester.email", is(itemRequestDto.getRequester().getEmail())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getOwnItemRequests() throws Exception {
        when(itemRequestService.getOwnItemRequests(any(Long.class),  eq(0), eq(10)))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(listOfItemRequestDtos))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].requester.id", is(itemRequestDto.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.[0].requester.name", is(itemRequestDto.getRequester().getName())))
                .andExpect(jsonPath("$.[0].requester.email", is(itemRequestDto.getRequester().getEmail())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getItemRequests() throws Exception {
        when(itemRequestService.getAllItemRequest(any(Long.class), any(Integer.class), nullable(Integer.class)))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(listOfItemRequestDtos))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].requester.id", is(itemRequestDto.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.[0].requester.name", is(itemRequestDto.getRequester().getName())))
                .andExpect(jsonPath("$.[0].requester.email", is(itemRequestDto.getRequester().getEmail())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}

