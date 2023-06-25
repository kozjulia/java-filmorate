package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.storage.GradeStorage;

import java.util.List;

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

    private Grade makeGrade(ResultSet rs, int rowNum) throws SQLException {
        return new Grade(rs.getLong("user_id"), rs.getInt("grade"));
    }

}