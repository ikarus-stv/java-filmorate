package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.Mpa;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@Slf4j
@Primary
@RequiredArgsConstructor

public class DBFilmStorage implements FilmStorage {
    private static final String FIND_ALL_QUERY = """
        SELECT F.ID,
               F.NAME_FILM,
               F.DESCRIPTION,
               F.RELEASE_DATE,
               F.DURATION,
               F.RATING_ID,
               R.NAME_RATING,
               LISTAGG(DISTINCT CONCAT(GENRE_ID, '/', G.NAME_GENRE)) FILTER (WHERE GENRE_ID IS NOT NULL) AS GENRES,
               LISTAGG(DISTINCT FL.USER_ID) AS LIKES
        FROM FILM F
        LEFT JOIN RATING R ON F.RATING_ID = R.ID
        LEFT JOIN FILM_GENRE FG ON F.ID = FG.FILM_ID
        LEFT JOIN GENRE G ON FG.GENRE_ID = G.ID
        LEFT JOIN FILM_LIKE FL ON F.ID = FL.FILM_ID
        GROUP BY F.ID,
                 F.NAME_FILM,
                 F.DESCRIPTION,
                 F.RELEASE_DATE,
                 F.DURATION,
                 F.RATING_ID,
                 R.NAME_RATING
                    """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT F.ID,
                   F.NAME_FILM,
                   F.DESCRIPTION,
                   F.RELEASE_DATE,
                   F.DURATION,
                   F.RATING_ID,
                   R.NAME_RATING,
                   LISTAGG(DISTINCT CONCAT(GENRE_ID, '/', G.NAME_GENRE)) FILTER (WHERE GENRE_ID IS NOT NULL) AS GENRES,
                   LISTAGG(DISTINCT FL.USER_ID) AS LIKES
            FROM FILM F
            LEFT JOIN RATING R ON F.RATING_ID = R.ID
            LEFT JOIN FILM_GENRE FG ON F.ID = FG.FILM_ID
            LEFT JOIN GENRE G ON FG.GENRE_ID = G.ID
            LEFT JOIN FILM_LIKE FL ON F.ID = FL.FILM_ID
            GROUP BY F.ID,
                     F.NAME_FILM,
                     F.DESCRIPTION,
                     F.RELEASE_DATE,
                     F.DURATION,
                     F.RATING_ID,
                     R.NAME_RATING
            HAVING F.ID = ?;
                """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        String insertFilmQuery = """
                INSERT INTO FILM(NAME_FILM, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)\s
                VALUES(?, ?, ?, ?, ?)
                """;
        String insertGenreQuery = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?,?)";

        checkFilm(film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertFilmQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        var filmGenres = film.getGenres();
        if (filmGenres != null) {
            filmGenres.forEach(g -> jdbcTemplate.update(insertGenreQuery, filmId, g.getId()));
        }

        return film;
    }

    @Override
    public Film update(Film newFilm) {
        String updateFilmQuery = """
                UPDATE FILM SET NAME_FILM = ?,
                DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ?
                WHERE ID = ?
                """;

        get(newFilm.getId());
        checkFilm(newFilm);

        jdbcTemplate.update(updateFilmQuery,
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate()),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
                );

        return newFilm;
    }

    @Override
    public Film get(Long id) {
        try {
            Film f = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, this::mapRowToFilm, id);
            return f;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    @Override
    public Collection<Genre> findAllGenres() {
        return jdbcTemplate.query("SELECT ID, NAME_GENRE FROM GENRE", this::mapRowToGenre);
    }

    @Override
    public Genre findGenreById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID, NAME_GENRE FROM GENRE WHERE ID=?",
                    this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с id=" + id + " не найден");
        }
    }

    @Override
    public Collection<Mpa> findAllMpa() {
        return jdbcTemplate.query("select ID, NAME_RATING from rating order by ID", this::mapRowToMpa);
    }

    @Override
    public Mpa findMpaById(Long id) {
        try {
            return jdbcTemplate.queryForObject("select ID, NAME_RATING from rating WHERE ID=?",
                    this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг с id=" + id + " не найден");
        }
    }

    @Override
    public void filmAddLike(Long filmId, Long userId) {
        jdbcTemplate.update("INSERT INTO FILM_LIKE (FILM_ID, USER_ID) VALUES (?, ?)", filmId, userId);
    }

    @Override
    public void filmRemoveLike(Long filmId, Long userId) {
        jdbcTemplate.update("DELETE FROM FILM_LIKE WHERE FILM_ID=? AND USER_ID=?", filmId, userId);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int i) throws SQLException  {
        return Mpa.builder()
                .id(resultSet.getLong("ID"))
                .name(resultSet.getString("NAME_RATING"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException  {
        return Genre.builder()
                .id(resultSet.getLong("ID"))
                .name(resultSet.getString("NAME_GENRE"))
                .build();
    }

    private Film mapRowToFilm(ResultSet resultSet, int i) throws SQLException  {
        Film film = new Film();
        film.setId(resultSet.getLong("ID"));
        film.setName(resultSet.getString("NAME_FILM"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(resultSet.getInt("DURATION"));
        film.setMpa(new Mpa(resultSet.getLong("RATING_ID"), resultSet.getString("NAME_RATING")));

        String genres = resultSet.getString("GENRES");
        HashSet<Genre> genreHashSet = new HashSet<>();
        if (genres != null) {
            String[] splitGenres = genres.split(",");
            for (String strGenre : splitGenres) {
                String[] genreRow = strGenre.split("/");
                Genre g = new Genre(Long.parseLong(genreRow[0]), genreRow[1]);
                genreHashSet.add(g);
            }
            film.setGenres(genreHashSet);
        }

        String likes = resultSet.getString("LIKES");
        if (likes != null) {
            film.setLikes(Arrays.stream(likes.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toSet()));
        }
        return film;
    }

    private void checkFilm(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate != null && releaseDate.isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

}
