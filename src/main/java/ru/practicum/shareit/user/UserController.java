package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAllUsers() {
        log.info("Request to search all users");
        return userService.findAllUser();
    }

    @GetMapping("/{userId}")
    public UserDto findUser(@PathVariable long userId) {
        log.info("Request to search user with ID = {}", userId);
        return userService.findUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody User user) {
        log.info("Request to create user  {}", user);
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody User user) {
        log.info("Request to update user with ID = {}", userId);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Request to delete user with ID = {}", userId);
        userService.deleteUser(userId);
    }

}
