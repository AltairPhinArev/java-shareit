package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {

    Long id;

    String name;

    String description;

    Boolean available;

    User owner;

    Long requestId;


    public Item() {
    }

    @Builder
    public Item(Long id, String name, String description, Boolean available, User owner, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
    }
}
