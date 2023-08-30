package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestService itemRequestService;

    static final String USER_ID = "X-Sharer-User-Id";

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @ResponseBody
    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(USER_ID) Long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAll(@RequestHeader(USER_ID) Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemRequest(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long itemRequestId,
                                             @RequestHeader(USER_ID) Long userId) {
        return itemRequestService.getItemRequestById(itemRequestId, userId);
    }


    @GetMapping
    public List<ItemRequestDto> getOwnItemRequests(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getOwnItemRequests(userId, from, size);
    }
}
