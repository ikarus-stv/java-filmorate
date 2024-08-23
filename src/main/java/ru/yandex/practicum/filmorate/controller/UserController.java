package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {

    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userStorage.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    void makeFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        userService.makeFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    void destroyFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        userService.destroyFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseBody Collection<User> getUserFriends(@PathVariable Long id) {
        return userService.gerUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseBody Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
