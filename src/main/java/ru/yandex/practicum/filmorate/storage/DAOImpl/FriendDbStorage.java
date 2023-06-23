package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public boolean addInFriends(User friendRequest, User friendResponse) {
        String sqlQuery = "merge into friends(request_friend_id,response_friend_id) values (?, ?)";
        return jdbcTemplate.update(sqlQuery, friendRequest.getId(), friendResponse.getId()) > 0;
    }

    public boolean deleteFromFriends(User friendRequest, User friendResponse) {
        String sqlQuery = "delete from friends " +
                "where request_friend_id = ? " +
                "and response_friend_id = ?;";
        return jdbcTemplate.update(sqlQuery, friendRequest.getId(), friendResponse.getId()) > 0;
    }

    public List<Long> findFriends(long id) {
        String sqlQuery = "select response_friend_id from friends " +
                "where request_friend_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("response_friend_id"), id);
    }

}