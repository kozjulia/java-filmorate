package ru.yandex.practicum.filmorate.storage.DAOImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.*;

@Repository
@Primary
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public boolean like(Film film) {
        for (long idUser : film.getLikes()) {
            String sqlQuery = "insert into likes(user_id, film_id) " +
                    " values (?, ?)";
            jdbcTemplate.update(sqlQuery, idUser, film.getId());
        }

        return true;
    }

    public boolean dislike(Film film) {
        String sqlQuery = "delete from likes where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return true;
    }

    public List<Long> findLikes(Film film) {
        String sqlQuery = "select * from likes where film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), film.getId());
    }

    public Map<Long, Set<Long>> findAllUsersWithLikes() {
        Map<Long, Set<Long>> usersWithLikes = new HashMap<>();
        String sqlQueryUsersId = "select user_id from likes group by user_id;";
        List<Long> users = jdbcTemplate.query(sqlQueryUsersId, (rs, rowNum) -> rs.getLong("user_id"));
        for (Long user : users) {
            String sqlQueryFilmsId = "select film_id from likes where user_id = ?;";
            List<Long> likes = jdbcTemplate.query(sqlQueryFilmsId,
                    (rs, rowNum) -> rs.getLong("film_id"), user);
            usersWithLikes.put(user, new HashSet<>(likes));
        }
        return usersWithLikes;
    }
}