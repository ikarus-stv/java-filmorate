package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor

public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<FilmDTO> findAll() {
        return filmStorage.findAll().stream().map(film -> FilmMapper.mapToFilmDTO(film)).toList();
    }

    @GetMapping("/genres")
    public Collection<Genre> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable("id") Long id) {
        return filmStorage.findGenreById(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findAllMpa(@PathVariable("id") Long id) {
        return filmStorage.findMpaById(id);
    }

    @GetMapping("/films/{id}")
    public FilmDTO findById(@PathVariable("id") Long id) {
        return FilmMapper.mapToFilmDTO(filmStorage.get(id));
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody NewFilmDTO request) {
        Film result = filmStorage.create(FilmMapper.mapNewFilmDTOToFilm(request));
        return result;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmStorage.update(newFilm);
    }

    @PutMapping("/films/{id}/like/{userId}")
    void filmAddLike(@PathVariable  Long id, @PathVariable Long userId) {
        filmService.filmAddLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    void filmRemoveLike(@PathVariable  Long id, @PathVariable Long userId) {
        filmService.filmRemoveLike(id, userId);
    }

    @GetMapping("/films/popular")
    @ResponseBody
    Collection<Film> getMostPopular(@RequestParam(name = "count", required = false, defaultValue = "10") Long count) {
        return filmService.getMostPopular(count);
    }
}
