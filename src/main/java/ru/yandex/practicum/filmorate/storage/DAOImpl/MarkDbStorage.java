package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mark;
import ru.yandex.practicum.filmorate.storage.MarkStorage;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Primary
@RequiredArgsConstructor
public class MarkDbStorage implements MarkStorage {

    private final JdbcTemplate jdbcTemplate;

    public boolean rate(long id, long userId, int mark) {
        String sqlQuery = "merge into marks (film_id, user_id, mark) " +
                " values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId, mark);
        return true;
    }

    public boolean unrate(long id, long userId) {
        String sqlQuery = "delete from marks where film_id = ? and user_id = ? ";
        jdbcTemplate.update(sqlQuery, id, userId);
        return true;
    }

    public List<Long> findLikes(Film film) {
        String sqlQuery = "select user_id from marks where film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), film.getId());
    }

    public List<Mark> findMarks(Film film) {
        String sqlQuery = "select * from marks where film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::makeMark, film.getId());
    }

    public Map<Long, Set<Long>> findAllUsersWithPositiveMarks() {
        Map<Long, Set<Long>> usersWithMarks = new HashMap<>();
        String sqlQueryUsersId = "select user_id from marks where mark > 5 group by user_id ";
        List<Long> users = jdbcTemplate.query(sqlQueryUsersId, (rs, rowNum) -> rs.getLong("user_id"));
        for (Long user : users) {
            String sqlQueryFilmsId = "select film_id from marks where user_id = ? and mark > 5 ";
            List<Long> marks = jdbcTemplate.query(sqlQueryFilmsId,
                    (rs, rowNum) -> rs.getLong("film_id"), user);
            usersWithMarks.put(user, new HashSet<>(marks));
        }
        return usersWithMarks;
    }

    private Mark makeMark(ResultSet rs, int rowNum) throws SQLException {
        return new Mark(rs.getLong("user_id"), rs.getInt("mark"));
    }

}