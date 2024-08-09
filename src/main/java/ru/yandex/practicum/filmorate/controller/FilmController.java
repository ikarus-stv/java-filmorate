package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        checkFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            newValidationException("Id должен быть указан");
        }

        if (!films.containsKey(newFilm.getId())) {
            newNotFoundException("Пользователь с Id=" + newFilm.getId() + " не найден");
        }

        Film oldFilm = films.get(newFilm.getId());

        checkFilm(newFilm);

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Обновлен фильм {}", oldFilm);

        return oldFilm;
    }


    private void checkFilm(Film film) {
        String name = film.getName();
        if (name == null || name.isEmpty() || name.isBlank()) {
            newValidationException("название не может быть пустым");
        }

        String description = film.getDescription();
        if (description != null && description.length() > 200) {
            newValidationException("максимальная длина описания — 200 символов");
        }

        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate != null && releaseDate.isBefore(LocalDate.of(1895,12,28))) {
            newValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        }

        Integer duration = film.getDuration();
        if (duration == null || duration < 0) {
            newValidationException("продолжительность фильма должна быть положительным числом");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
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
