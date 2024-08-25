package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;

    public void makeFriendship(Long id1, Long id2) {
        if (id1.equals(id2)) {
            return;
        }
        User user1 = storage.get(id1);
        User user2 = storage.get(id2);
        user1.getFriends().add(id2);
        user2.getFriends().add(id1);
    }

    public void destroyFriendship(Long id1, Long id2) {
        User user1 = storage.get(id1);
        User user2 = storage.get(id2);
        user1.getFriends().remove(id2);
        user2.getFriends().remove(id1);
    }

    public Collection<User> getCommonFriends(Long id1, Long id2) {
        User user1 = storage.get(id1);
        User user2 = storage.get(id2);

        Set<Long> resultSet = new HashSet<>(user1.getFriends());
        resultSet.retainAll(user2.getFriends());

        List<User> result = resultSet.stream().map(storage::get).toList();

        return result;
    }

    public Collection<User> gerUserFriends(Long id) {
        User user = storage.get(id);
        return user.getFriends().stream().map(storage::get).toList();
    }
}
