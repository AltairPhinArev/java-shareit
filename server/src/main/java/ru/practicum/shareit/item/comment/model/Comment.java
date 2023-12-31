package ru.practicum.shareit.item.comment.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "comment_id")
   private Long id;

   @Column(name = "text")
   private String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "created")
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