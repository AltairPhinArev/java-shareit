package ru.practicum.shareit.negativeTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class NegativeUserTest {

    @Mock
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    public void shouldExceptionWhenGetUserWithWrongId() {
        userService = new UserServiceImpl(userRepository);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(-10L));
        Assertions.assertEquals("User with ID= -10  doesn't exist", exception.getMessage());
    }

    @Test
    public void testErrorCreateUser() {
        UserDto userDto = UserDto.builder().build();
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userService.createUser(userDto));
    }

    @Test
    public void testErrorDeleteUser() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.deleteUserById(100L));

        Assertions.assertEquals("User with ID= 100 doesn't exist", exception.getMessage());
    }
}
