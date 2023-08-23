package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester() != null ? itemRequest.getRequester() : null,
                itemRequest.getCreated(),
                items
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemDto) {
        return new ItemRequest(
                itemDto.getId(),
                itemDto.getDescription(),
                itemDto.getRequester() != null ? itemDto.getRequester() : null,
                itemDto.getCreated()
        );
    }

    public static ItemRequest toCreatedItemRequest(ItemRequestDto itemDto, User user, LocalDateTime created) {
        return new ItemRequest(
                null,
                itemDto.getDescription(),
                user,
                created
        );
    }
}