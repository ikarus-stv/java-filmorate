package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    User get(Long id);

    void addFriendship(Long id1, Long id2);

    void acceptFriendship(Long id1, Long id2);

    void destroyFriendship(Long id1, Long id2);
}

