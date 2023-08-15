package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.InputCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDtoFull> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoFull getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByDescription(@RequestParam String text,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getItemByDescription(text, from, size);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @ResponseBody
    @PostMapping(value = "/{itemId}/comment")
    public CommentDto createComment(@RequestBody InputCommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId) {
        return itemService.createNewComment(commentDto, userId, itemId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }
}
