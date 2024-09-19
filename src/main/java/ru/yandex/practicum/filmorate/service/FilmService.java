package ru.yandex.practicum.filmorate.service;

//import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.FilmMapper;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, @Qualifier("DBUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void filmAddLike(Long filmId, Long userId) {
        filmStorage.get(filmId);
        userStorage.get(userId);

        filmStorage.filmAddLike(filmId, userId);
    }

    public void filmRemoveLike(Long filmId, Long userId) {
        filmStorage.filmRemoveLike(filmId, userId);
    }

    public List<Film> getMostPopular(Long count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> (f2.getLikes().size() - f1.getLikes().size()))
                .limit(count)
                .toList();
    }

    public FilmDTO update(UpdateFilmDTO newFilm) {
        Film updatedFilm = filmStorage.get(newFilm.getId());
        return FilmMapper.mapToFilmDTO(filmStorage.update(FilmMapper.updateFilmFields(updatedFilm, newFilm)));
    }
}
