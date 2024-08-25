package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void filmAddLike(Long filmId, Long userId) {
        Film film = filmStorage.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        User user = userStorage.get(userId);

        film.getLikes().add(userId);
    }

    public void filmRemoveLike(Long filmId, Long userId) {
        Film film = filmStorage.get(filmId);

        User user = userStorage.get(userId);

        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopular(Long count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> (f2.getLikes().size() - f1.getLikes().size()))
                .limit(count)
                .toList();
    }

}
