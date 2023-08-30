package ru.practicum.shareit.itemRequest.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {

    Long id;

    String description;

    User requester;

    LocalDateTime created;

    @Builder
    public ItemRequest(Long id, String description, User requester, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}
