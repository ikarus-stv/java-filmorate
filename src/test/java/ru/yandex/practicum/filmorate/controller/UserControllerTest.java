package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController cntrl;

    @BeforeEach
    void prepare() {
        cntrl = new UserController();
    }

    @Test
    void trueUserAppends() {
        User user = new User();

        user.setEmail("xx@xx");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        cntrl.create(user);

        Collection<User> all = cntrl.findAll();
        assertEquals(1, all.size());
    }

    @Test
    void blankEmailFails() {
        User user = new User();

        user.setEmail("");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        ValidationException e = assertThrows(ValidationException.class, () -> cntrl.create(user));
        assertEquals("Электронная почта не может быть пустой", e.getMessage());

    }

    @Test
    void emailWithoutAtFails() {
        User user = new User();

        user.setEmail("XXXXXXXXXXXXX");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        ValidationException e = assertThrows(ValidationException.class, () -> cntrl.create(user));
        assertEquals("электронная почта должна содержать символ @", e.getMessage());
    }

    @Test
    void blankLoginFails() {
        User user = new User();

        user.setEmail("XXXXX@XXXXXXXX");
        user.setLogin(" ");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        ValidationException e = assertThrows(ValidationException.class, () -> cntrl.create(user));
        assertEquals("логин не может быть пустым и содержать пробелы", e.getMessage());
    }


    @Test
    void futureDateFails() {
        User user = new User();

        user.setEmail("XXXXX@XXXXXXXX");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(2995, 10,12));

        ValidationException e = assertThrows(ValidationException.class, () -> cntrl.create(user));
        assertEquals("дата рождения не может быть в будущем", e.getMessage());
    }

}
