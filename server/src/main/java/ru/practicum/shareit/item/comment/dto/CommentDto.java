package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;


@Data
public class CommentDto {

    private Long id;

    private String text;

    private Item item;

    private String authorName;

    private LocalDateTime created;

    @Builder
    public CommentDto(Long id, String text, Item item, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.item = item;
        this.authorName = authorName;
        this.created = created;
    }
}
