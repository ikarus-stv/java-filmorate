package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return Collections.unmodifiableCollection(filmStorage.findAll());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmStorage.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    void filmAddLike(@PathVariable  Long id, @PathVariable Long userId) {
        filmService.filmAddLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void filmRemoveLike(@PathVariable  Long id, @PathVariable Long userId) {
        filmService.filmRemoveLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseBody
    Collection<Film> getMostPopular(@RequestParam(name = "count", required = false, defaultValue = "10") Long count) {
        return filmService.getMostPopular(count);
    }
}
