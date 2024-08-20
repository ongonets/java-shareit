package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dal.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<User> findAllUser() {
        return userStorage.findAllUser();
    }

    @Override
    public User findUserById(long userId) {
        return userStorage.findUserById(userId)
                .orElseThrow(() -> {
                    log.error("Not found user with ID = {}", userId);
                    return new NotFoundException(String.format("Not found user with ID = %d", userId));
                });
    }

    @Override
    public User createUser(User user) {
        validateEmail(user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        User oldUser = findUserById(userId);
        if (user.getEmail() != null) {
            validateEmail(user);
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        return userStorage.updateUser(oldUser);
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }

    private void validateEmail(User user) {
        String email = user.getEmail();
        if (email == null || !(email.matches("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$"))) {
            log.error("Email was entered incorrectly by user {}", user);
            throw new ValidationException("Email was entered incorrectly");
        }
        userStorage.findAllUser().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .ifPresent(user1 -> {
                    log.error("Email was duplicate by user {}", user);
                    throw new DuplicateDataException("Email was duplicate");
                });
    }
}
