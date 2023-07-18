package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
public class UserService {

    UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<UserDto> getAllUsers() {
        return userStorage.getAll();
    }

    public UserDto createUser(UserDto user) {
        return userStorage.createUser(user);
    }

    public UserDto updateUser(UserDto user, Long userId) {
        return userStorage.updateUser(user, userId);
    }

    public UserDto getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public void deleteUser(Long userId) {
        userStorage.deleteUserById(userId);
    }
}
