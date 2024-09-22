package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Primary
@Component("DBUserStorage")
public class DBUserStorage implements UserStorage {
    private static final String FIND_ALL_QUERY = """
                SELECT ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY, LISTAGG(f.USER2_ID) AS FRIENDS
                FROM USERS u
                LEFT JOIN FRIENDSHIP f ON u.ID = f.USER1_ID AND f.CONFIRMED
                GROUP BY ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY, CREATED_AT
                """;
    private static final String FIND_BY_ID_QUERY = """
                SELECT ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY, LISTAGG(f.USER2_ID) AS FRIENDS
                FROM USERS u
                LEFT JOIN FRIENDSHIP f ON u.ID = f.USER1_ID AND f.CONFIRMED
                WHERE u.ID = ?
                GROUP BY ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY, CREATED_AT
                """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, this::mapRowToUser);
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User newUser) {
        String sqlQuery = """
                UPDATE USERS SET
                EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?
                WHERE ID = ?
                """;
        int rowsAffected = jdbcTemplate.update(sqlQuery,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId());
        if (rowsAffected == 0) {
            throw new NotFoundException("Пользователь с id=" + newUser.getId() + " не найден");
        }
        return newUser;
    }

    @Override
    public User get(Long id) {
        try {
            User u = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, this::mapRowToUser, id);
            return u;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    @Override
    public void addFriendship(Long id1, Long id2) {
        String sqlQuery = "INSERT INTO FRIENDSHIP (USER1_ID, USER2_ID, CONFIRMED) VALUES (?, ?, TRUE)";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public void acceptFriendship(Long id1, Long id2) {
        String sqlQuery = "UPDATE FRIENDSHIP SET CONFIRMED = TRUE WHERE USER1_ID = ? AND USER2_ID = ?";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public void destroyFriendship(Long id1, Long id2) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER1_ID = ? AND USER2_ID = ?";
        int count = jdbcTemplate.update(sqlQuery, id1, id2);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum)  throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("ID"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setName(resultSet.getString("USER_NAME"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());

        String friends = resultSet.getString("FRIENDS");

        if (friends != null) {
            String[] friendsSplitted = friends.split(",");
            user.setFriends(Arrays.stream(friendsSplitted).map(s -> Long.parseLong(s)).collect(Collectors.toSet()));
        }
        return user;
    }
}
