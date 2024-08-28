package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAllUser();

    Optional<User> findUserById(long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);
}
