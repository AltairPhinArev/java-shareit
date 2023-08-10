package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RegisterException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validate(userDto);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID=" + userId + " doesn't exist"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (!Objects.equals(userDto.getEmail(), null) && (!userDto.getEmail().equals(user.getEmail()))) {
            List<User> usersWithEmail = userRepository.findByEmail(userDto.getEmail());

            boolean allMatch = usersWithEmail.stream()
                    .allMatch(u -> u.getId().equals(userDto.getId()));

            if (allMatch) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new RegisterException("User with E-mail=" + userDto.getEmail() + " exists");
            }

        }

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID= " + userId + "  doesn't exist")));
    }

    @Override
    public boolean checkUser(Long userId) {
        if (userRepository.existsById(userId)) {
            return true;
        } else {
            throw new NotFoundException("User with ID= " + userId + " doesn't exist");
        }
    }

    private void validate(UserDto user) {
        if (user.getEmail() != null && user.getEmail().contains("@") && !user.getName().isBlank()) {
            for (User userDto : userRepository.findAll()) {
                if (userDto.getEmail().equals(user.getEmail()) && !Objects.equals(userDto.getId(), user.getId())) {
                    userDto.setEmail("null");
                }
            }
        } else {
            throw new ValidationException("Illegal arguments for user");
        }
    }
}
