package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users;

    public UserStorageImpl() {
        users = new HashMap<>();
    }

    @Override
    public Collection<User> findAllUser() {
        return users.values();
    }

    @Override
    public Optional<User> findUserById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private long getId() {
        return users.keySet().stream().max(Long::compareTo).map(aLong -> aLong + 1).orElse(1L);
    }
}
