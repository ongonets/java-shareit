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
    private UserStorage userStorage;

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
    public User updateUser(User user) {
        findUserById(user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public void deleteUSer(long userId) {
        userStorage.deleteUSer(userId);
    }

    private void validateEmail(User user) {
        String email = user.getEmail();
        if (!(email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))) {
            log.error("Email was entered incorrectly by user {}", user);
            throw new ValidationException("Email was entered incorrectly");
        }
        userStorage.findAllUser().stream()
                .map(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .ifPresent(user1 -> {
                    log.error("Email was entered incorrectly by user {}", user);
                    throw new DuplicateDataException("Email was entered incorrectly");
                });
    }
}
