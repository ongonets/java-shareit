package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> findAllUser();

    User findUserById(long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUSer(long userId);
}
