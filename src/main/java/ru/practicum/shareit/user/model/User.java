package ru.practicum.shareit.user.model;

import lombok.*;
import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {

    Long id;

    String name;

    @Email
    String email;

    @Builder
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
