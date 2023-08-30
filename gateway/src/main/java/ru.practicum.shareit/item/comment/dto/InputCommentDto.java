package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InputCommentDto {

    Long id;

    String text;

    Long itemId;

    LocalDateTime created;

    @Builder
    public InputCommentDto(Long id, String text, Long itemId, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.created = created;
    }
}
