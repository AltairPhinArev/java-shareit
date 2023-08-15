package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @ResponseBody
    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemRequest(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long itemRequestId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestById(itemRequestId, userId);
    }


    @GetMapping
    public List<ItemRequestDto> getOwnItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getOwnItemRequests(userId, from, size);
    }


    @DeleteMapping("/{itemRequestId}")
    public void delete(@PathVariable Long itemRequestId) {
        itemRequestService.deleteItemRequestById(itemRequestId);
    }
}
