package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "item_request")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;

    @ManyToOne()
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    User requester;

    LocalDateTime created;

    public ItemRequest() {

    }

    @Builder
    public ItemRequest(Long id, String description, User requester, LocalDateTime created) {
        this.id = id;
        this.description = description;

        this.requester = requester;
        this.created = created;
    }
}
