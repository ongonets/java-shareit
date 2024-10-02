package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid NewUserRequestDto newUser) {
        log.info("Request to create user  {}", newUser);
        return userClient.createUser(newUser);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Request to search all users");
        return userClient.getAllUser();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUser(@PathVariable long userId) {
        log.info("Request to search user with ID = {}", userId);
        return userClient.findUserById(userId);
    }



    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId, @RequestBody UpdateUserRequestDto user) {
        log.info("Request to update user with ID = {}", userId);
        return userClient.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Request to delete user with ID = {}", userId);
        return userClient.deleteUser(userId);
    }
}