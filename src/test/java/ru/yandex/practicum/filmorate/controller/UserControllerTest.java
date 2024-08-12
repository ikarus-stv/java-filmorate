package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.ConstraintViolation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController cntrl;
    private Validator validator;

    @BeforeEach
    void prepare() {
        cntrl = new UserController();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void trueUserAppends() {
        User user = new User();

        user.setEmail("xx@xx");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());

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

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Электронная почта не может быть пустой")));

    }

    @Test
    void emailWithoutAtFails() {
        User user = new User();

        user.setEmail("XXXXXXXXXXXXX");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Неверный формат адреса электронной почты")));

    }

    @Test
    void blankLoginFails() {
        User user = new User();

        user.setEmail("XXXXX@XXXXXXXX");
        user.setLogin(" ");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1995, 10,12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Логин не может быть пустым и содержать пробелы")));
    }


    @Test
    void futureDateFails() {
        User user = new User();

        user.setEmail("XXXXX@XXXXXXXX");
        user.setLogin("Login");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(2995, 10,12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("не может быть в будущем")));

    }

}
