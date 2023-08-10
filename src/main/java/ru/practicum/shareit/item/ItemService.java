package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.InputCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    CommentDto createNewComment(InputCommentDto inputCommentDto, Long userId, Long itemId);

    Collection<ItemDtoFull> getAllItems(Long userId);

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    void deleteItem(Long itemId);

    ItemDtoFull getItemById(Long itemId, Long userId);

    ItemDto getItem(Long itemId);

    List<ItemDto> getItemByDescription(String description);

    boolean checkItem(Long itemId);
}
