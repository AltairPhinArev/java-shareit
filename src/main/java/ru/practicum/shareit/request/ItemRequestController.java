package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequestDto> getAll() {
        return itemRequestService.getAllItemRequest();
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long itemRequestId) {
        return itemRequestService.getItemRequestById(itemRequestId);
    }

    @ResponseBody
    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(itemRequestDto);
    }

    @ResponseBody
    @PatchMapping
    public ItemRequestDto updateUser(@RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.updateItemRequest(itemRequestDto);
    }

    @DeleteMapping("/{itemRequestId}")
    public void delete(@PathVariable Long itemRequestId) {
        itemRequestService.deleteItemRequestById(itemRequestId);
    }
}
