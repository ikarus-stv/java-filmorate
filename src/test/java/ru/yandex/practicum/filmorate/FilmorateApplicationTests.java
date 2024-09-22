package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.Mpa;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DBFilmStorage;
import ru.yandex.practicum.filmorate.storage.DBUserStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")

public class FilmorateApplicationTests {
	private final UserStorage userStorage;
	private final FilmStorage filmStorage;

	@Test
	public void testFindUserById() {

		Optional<User> userOptional = Optional.of(userStorage.get(1L));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	public void testUserFindAll() {
		Collection<User> all = userStorage.findAll();
		assertTrue(all.size() > 0);
	}


	@Test
	public void testUserCreate() {
		User user = new User();

		user.setEmail("xx@xx");
		user.setLogin("Login");
		user.setName("Имя");
		user.setBirthday(LocalDate.of(1995, 10,12));

		userStorage.create(user);
		assertTrue(user.getId() > 0);
	}

	@Test
	void testFilmCreate() {
		Film film = new Film();

		film.setName("Война и Мир");
		film.setDescription("Про войну и мир");
		film.setDuration(100);
		film.setReleaseDate(LocalDate.of(1995, 10,12));
		film.setMpa(new Mpa(1L, null));

		filmStorage.create(film);
		assertTrue(film.getId() > 0);
	}

	public void testFilmFindAll() {
		Collection<Film> all = filmStorage.findAll();
		assertTrue(all.size() > 0);
	}

	@Test
	public void testFindFilmById() {

		Optional<Film> filmOptional = Optional.of(filmStorage.get(1L));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}


	@Autowired
	public FilmorateApplicationTests(DBUserStorage userStorage, DBFilmStorage filmStorage) {
		this.userStorage = userStorage;
		this.filmStorage = filmStorage;
	}
}