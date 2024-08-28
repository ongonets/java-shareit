package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAllUser();

    UserDto findUserById(long userId);

    UserDto createUser(NewUserRequest newUserRequest);

    UserDto updateUser(long userId, User user);

    void deleteUser(long userId);
}
