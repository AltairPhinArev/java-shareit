package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RegisterException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    UserMapper mapper;
    Long userId = 1L;
    Map<Long, UserDto> userMap = new HashMap<>();

    public InMemoryUserStorage(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDto createUser(UserDto user) {
        validate(user);
        user.setId(userId++);
        userMap.put(user.getId(), user);
        log.info("User has been created with id= {}", user.getId());
        return user;
    }

    @Override
    public UserDto updateUser(UserDto updatedUser, Long userId) {
        if (userMap.containsKey(userId)) {

            UserDto currentUser = userMap.get(userId);
            updatedUser.setId(currentUser.getId());

            if (updatedUser.getEmail() == null) {
                updatedUser.setEmail(currentUser.getEmail());
            } else if (updatedUser.getName() == null) {
                updatedUser.setName(currentUser.getName());
            }

            validate(updatedUser);
            userMap.put(updatedUser.getId(), updatedUser);
            log.info("User has been updated with id= {}", updatedUser.getId());
            return updatedUser;
        } else {
            throw new ValidationException("User with id=" + userId + " doesn't exist");
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
            log.info("User has been removed with id= {}", userId);
        } else {
            log.error("Couldn't find user with id= {}", userId);
            throw new ValidationException("User with id=" + userId + " doesn't exist");
        }
    }

    @Override
    public Collection<UserDto> getAll() {
        return userMap.values();
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userMap.containsKey(userId)) {
            log.info("User with id= {} from = {}", userId, userMap.size());
            return userMap.get(userId);
        } else {
            log.error("Couldn't find user with id= {}", userId);
            throw new NotFoundException("User with id=" + userId + " doesn't exist");
        }
    }

    private void validate(UserDto user) {
        if (user.getEmail() != null && user.getEmail().contains("@") && !user.getName().isBlank()) {
            for (UserDto userDto : userMap.values()) {
                if (userDto.getEmail().equals(user.getEmail()) && !Objects.equals(userDto.getId(), user.getId())) {
                    throw new RegisterException("User with this email already exist " + user.getEmail());
                }
            }
        } else {
            throw new ValidationException("Illegal arguments for user");
        }
    }
}
