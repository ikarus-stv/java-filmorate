package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.Mpa;

/**
 * Film.
 */
@Data
public class Film {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;                // целочисленный идентификатор
    @NotEmpty(message = "Название не может быть пустым")
    private String name;            // название
    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;     // описание
    private LocalDate releaseDate;  // дата релиза
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;      // продолжительность фильма
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Long> likes = new HashSet<>();

}
