package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.Mpa;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film get(Long id);

    Collection<Genre> findAllGenres();

    Genre findGenreById(Long id);

    Collection<Mpa> findAllMpa();

    Mpa findMpaById(Long id);

    void filmAddLike(Long filmId, Long userId);

    void filmRemoveLike(Long filmId, Long userId);
}
