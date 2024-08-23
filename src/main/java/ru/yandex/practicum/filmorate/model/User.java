package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;                // целочисленный идентификатор
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Неверный формат адреса электронной почты")
    private String email;           // электронная почта
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private String login;           // логин пользователя
    private String name;            // имя для отображения
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;     // дата рождения
    private Set<Long> friends = new HashSet<>();
}

