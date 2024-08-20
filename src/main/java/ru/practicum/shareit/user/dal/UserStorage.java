package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAllUser();

    Optional<User> findUserById(long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUSer(long userId);
}
