package ru.practicum.shareit.item.comment.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Comment {

   private Long id;

   private String text;

    private Item item;

    private User author;

    private LocalDateTime created;

    public Comment() {
    }

    @Builder
    public Comment(Long id, String text, Item item, User author, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.item = item;
        this.author = author;
        this.created = created;
    }
}