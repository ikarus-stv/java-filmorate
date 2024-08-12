package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long currentMaxId = 0;


    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        checkUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            newValidationException("Id пользователя должен быть указан");
        }

        if (!users.containsKey(newUser.getId())) {
            newNotFoundException("Пользователь с Id=" + newUser.getId() + " не найден");
        }

        User oldUser = users.get(newUser.getId());

        checkUser(newUser);

        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Обновлен пользователь {}", oldUser);

        return oldUser;
    }

    private long getNextId() {
        return ++currentMaxId;
    }

    private void checkUser(User user) {
        String email = user.getEmail();
        String login = user.getLogin();

        String name = user.getName();
        if (name == null || name.isBlank() || name.isEmpty()) {
            user.setName(login);
        }
    }

    private void newValidationException(String errMsg) {
        log.error(errMsg);
        throw new ValidationException(errMsg);
    }

    private void newNotFoundException(String errMsg) {
        log.error(errMsg);
        throw new NotFoundException(errMsg);
    }

}
