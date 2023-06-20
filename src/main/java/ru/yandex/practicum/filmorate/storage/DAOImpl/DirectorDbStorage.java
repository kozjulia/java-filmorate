package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

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
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<Director> findDirectors() {
        String sqlQuery = "select * from directors";
        return jdbcTemplate.query(sqlQuery, this::makeDirector);
    }

    public Optional<Director> findDirectorById(long directorId) {
        String sqlQuery = "select * from directors where director_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::makeDirector, directorId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean isFindDirectorById(long directorId) {
        String sqlQuery = "select exists(select 1 from directors where director_id = ?)";
        if (jdbcTemplate.queryForObject(sqlQuery, Boolean.class, directorId)) {
            return true;
        }
        log.warn("Режиссёр № {} не найден", directorId);
        throw new DirectorNotFoundException(String.format("Режиссёр № %d не найден", directorId));
    }

    public Optional<Director> createDirector(Director director) {
        String sqlQuery = "insert into directors(director_name) " +
                " values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        director.setId(keyHolder.getKey().longValue());
        return Optional.of(director);
    }

    public Optional<Director> updateDirector(Director director) {
        String sqlQuery = "update directors set director_name = ? where director_id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return Optional.of(director);
    }

    public boolean deleteDirectorById(Long directorId) {
        String sqlQuery = "delete from directors where director_id = ?";
        return jdbcTemplate.update(sqlQuery, directorId) > 0;
    }

    public Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("director_id"), rs.getString("director_name"));
    }

}