package ru.yandex.practicum.filmorate.service;

//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage storage;

    public UserService(/*@Qualifier("DBUserStorage") */UserStorage storage) {
        this.storage = storage;
    }

    public void makeFriendship(Long id1, Long id2) {
        if (id1.equals(id2)) {
            return;
        }
        User user1 = storage.get(id1);
        User user2 = storage.get(id2);
        if (!user1.getFriends().contains(id2)) {
            storage.addFriendship(id1, id2);
        }
    }

    public void acceptFriendship(Long id1, Long id2) {
        storage.acceptFriendship(id1, id2);
    }


    public void destroyFriendship(Long id1, Long id2) {
        storage.get(id1);
        storage.get(id2);
        storage.destroyFriendship(id1, id2);
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
