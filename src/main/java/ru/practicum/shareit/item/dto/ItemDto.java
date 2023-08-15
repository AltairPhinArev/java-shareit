package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import ru.practicum.shareit.user.model.User;

@Data
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    User owner;

    Long requestId;

    @Builder
    public ItemDto(Long id, String name, String description, Boolean available, User owner, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
    }
}