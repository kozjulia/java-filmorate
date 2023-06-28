package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.storage.GradeStorage;

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
public class GradeDbStorage implements GradeStorage {

    private final JdbcTemplate jdbcTemplate;

    public boolean rate(long id, long userId, int grade) {
        String sqlQuery = "merge into grades (film_id, user_id, grade) " +
                " values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId, grade);
        return true;
    }

    public boolean unrate(long id, long userId) {
        String sqlQuery = "delete from grades where film_id = ? and user_id = ? ";
        jdbcTemplate.update(sqlQuery, id, userId);
        return true;
    }

    public List<Grade> findGrades(Film film) {
        String sqlQuery = "select * from grades where film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::makeGrade, film.getId());
    }

    public Map<Long, Set<Long>> findAllUsersWithPositiveGrades() {
        Map<Long, Set<Long>> usersWithGrades = new HashMap<>();
        String sqlQueryUsersId = "select user_id from grades where grade > 5 group by user_id ";
        List<Long> users = jdbcTemplate.query(sqlQueryUsersId, (rs, rowNum) -> rs.getLong("user_id"));
        for (Long user : users) {
            String sqlQueryFilmsId = "select film_id from grades where user_id = ? and grade > 5 ";
            List<Long> grades = jdbcTemplate.query(sqlQueryFilmsId,
                    (rs, rowNum) -> rs.getLong("film_id"), user);
            usersWithGrades.put(user, new HashSet<>(grades));
        }
        return usersWithGrades;
    }

    private Grade makeGrade(ResultSet rs, int rowNum) throws SQLException {
        return new Grade(rs.getLong("user_id"), rs.getInt("grade"));
    }

}