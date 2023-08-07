package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {


    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user,  Long userId);

    void deleteUserById(Long userId);

    Collection<UserDto> getAll();

    UserDto getUserById(Long userId);

    boolean checkUser(Long userId);
}
