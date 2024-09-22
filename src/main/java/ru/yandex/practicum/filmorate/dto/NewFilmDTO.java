package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class NewFilmDTO {
    @NotEmpty(message = "Название не может быть пустым")
    public String name;
    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов")
    public String description;
    public LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    public int duration;
    public Mpa mpa;
    public ArrayList<Genre> genres;
}
