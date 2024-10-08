package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentMaxId = 0;

    @Override
    public Collection<User> findAll() {
        return Collections.unmodifiableCollection(users.values());
    }

    @Override
    public User create(User user) {
        checkUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    private void checkUser(User user) {
        String email = user.getEmail();
        String login = user.getLogin();

        String name = user.getName();
        if (name == null || name.isBlank() || name.isEmpty()) {
            user.setName(login);
        }
    }

    @Override
    public User update(User newUser) {
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

    @Override
    public User get(Long id) {
        User result = users.get(id);
        if (result == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return result;
    }

    @Override
    public void addFriendship(Long id1, Long id2) {
        get(id1).getFriends().add(id2);
    }

    @Override
    public void acceptFriendship(Long id1, Long id2) {
        get(id1).getFriends().add(id2);
    }

    @Override
    public void destroyFriendship(Long id1, Long id2) {
        get(id1).getFriends().remove(id2);
    }

    private void newValidationException(String errMsg) {
        log.error(errMsg);
        throw new ValidationException(errMsg);
    }

    private void newNotFoundException(String errMsg) {
        log.error(errMsg);
        throw new NotFoundException(errMsg);
    }

    private long getNextId() {
        return ++currentMaxId;
    }

}
