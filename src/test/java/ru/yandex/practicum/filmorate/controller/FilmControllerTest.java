package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController cntrl;
    private Validator validator;


    @BeforeEach
    void prepare() {
//        cntrl = new FilmController();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void trueFilmAppends() {
        Film film = new Film();

        film.setName("Война и Мир");
        film.setDescription("Про войну и мир");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1995, 10,12));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());

        cntrl.create(film);

        Collection<Film> all = cntrl.findAll();
        assertEquals(1, all.size());
    }

    @Test
    void emptyNameFails() {
        Film film = new Film();

        film.setDescription("Про войну и мир");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1995, 10,12));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Название не может быть пустым")));

    }

    @Test
    void descrLonger200Fails() {
        Film film = new Film();

        film.setName("Война и Мир");
        film.setDescription("*".repeat(201));
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1995, 10,12));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Максимальная длина описания — 200 символов")));
    }

    @Test
    void earlyDateFails() {
        Film film = new Film();

        film.setName("Война и Мир");
        film.setDescription("*".repeat(200));
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895, 12,27));

        ValidationException e = assertThrows(ValidationException.class, () -> cntrl.create(film));
        assertTrue(e.getMessage().contains("дата релиза не может быть раньше"));
    }

    @Test
    void negativeDurationFails() {
        Film film = new Film();

        film.setName("Война и Мир");
        film.setDescription("*".repeat(200));
        film.setDuration(-1);
        film.setReleaseDate(LocalDate.of(1895, 12,28));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Продолжительность фильма должна быть положительным числом")));
    }
}

