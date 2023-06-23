package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> create(User user) {
        String sqlQuery = "insert into users(user_name, email, login, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return Optional.of(user);
    }

    public Optional<User> update(User user) {
        String sqlQuery = "update users set user_name = ?, email = ?, login = ?, birthday = ?" +
                " where user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(),
                user.getBirthday(), user.getId());
        return Optional.of(user);
    }

    public boolean delete(User user) {
        String sqlQuery = "delete from users where user_id = ?";
        return jdbcTemplate.update(sqlQuery, user.getId()) > 0;
    }

    public boolean deleteUserById(long userId) {
        String sqlQuery = "delete from users where user_id = ?";
        return jdbcTemplate.update(sqlQuery, userId) > 0;
    }

    public List<User> findUsers() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    public Optional<User> findUserById(long userId) {
        String sqlQuery = "select * from users where user_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::makeUser, userId));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Пользователь № {} не найден", userId);
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
    }

    public boolean isFindUserById(long userId) {
        String sqlQuery = "select exists(select 1 from users where user_id = ?)";
        if (jdbcTemplate.queryForObject(sqlQuery, Boolean.class, userId)) {
            return true;
        }
        log.warn("Пользователь № {} не найден", userId);
        throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
    }

    @Override
    public List<Event> getUserEvent(Integer id) {
        String sqlQuery = "SELECT * FROM feeds WHERE user_id = ?";
        return jdbcTemplate.query(sqlQuery, this::makeEvent, id);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getString("email"), rs.getString("login"),
                rs.getDate("birthday").toLocalDate());
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("user_name"));
        return user;
    }

    private Event makeEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getLong("user_id"))
                .eventType(rs.getString("event_type"))
                .operation(rs.getString("operation"))
                .eventId(rs.getLong("event_id"))
                .entityId(rs.getLong("entity_id"))
                .build();
    }

}