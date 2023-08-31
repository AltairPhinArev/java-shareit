package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                         @RequestHeader(USER_ID) Long requestorId) {
        log.info("POST - request to create Request = {} by User with ID = {}", itemRequestDto, requestorId);
        return itemRequestClient.create(itemRequestDto, requestorId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long itemRequestId,
                                                     @RequestHeader(USER_ID) Long userId) {
        log.info("GET - request from User with ID = {} to ItemRequest with ID = {}", userId, itemRequestId);
        return itemRequestClient.getItemRequestById(userId, itemRequestId);
    }


    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(@RequestHeader(USER_ID) Long userId) {
        log.info("GET - request from User with ID = {} to get OWN ItemRequest", userId);
        return itemRequestClient.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(USER_ID) Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @RequestParam(required = false) Integer size) {
        log.info("GET - request from User with ID = {} to get ALL Requests with SIZE {}", userId, size);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }
}
