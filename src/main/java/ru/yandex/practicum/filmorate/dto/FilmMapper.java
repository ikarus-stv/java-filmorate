package ru.yandex.practicum.filmorate.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static Film mapNewFilmDTOToFilm(NewFilmDTO request) {
        Film film = new Film();
	    film.setName(request.name);
        film.setDescription(request.description);
        film.setReleaseDate(request.releaseDate);
        film.setDuration(request.duration);
        film.setMpa(new Mpa(request.getMpa().getId(), null));
        var genres = request.getGenres();
        if (genres != null) {
            genres.forEach(genre -> film.getGenres().add(new Genre(genre.getId(), genre.getName())));
        }
        return film;
    }

    public static FilmDTO mapToFilmDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(new Mpa(film.getMpa().getId(), film.getMpa().getName()));
        dto.setGenres(film.getGenres().stream().map(genre -> new Genre(genre.getId(), genre.getName())).toList());
        return dto;
    }


/*
    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasPassword()) {
            user.setPassword(request.getPassword());
        }
        if (request.hasUsername()) {
            user.setUsername(request.getUsername());
        }
        return user;
    }
*/
}