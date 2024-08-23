package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentMaxId = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        checkFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
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

    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    private void newNotFoundException(String errMsg) {
        log.error(errMsg);
        throw new NotFoundException(errMsg);
    }

    private long getNextId() {
        return ++currentMaxId;
    }

    private void newValidationException(String errMsg) {
        log.error(errMsg);
        throw new ValidationException(errMsg);
    }

    private void checkFilm(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate != null && releaseDate.isBefore(LocalDate.of(1895,12,28))) {
            newValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}
