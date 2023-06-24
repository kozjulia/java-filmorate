package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.sql.Timestamp;
import java.time.Instant;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("eventDbStorage")
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createEvent(long userId, String eventType, String operation, long entityId) {

        long timestamp = Timestamp.from(Instant.now()).getTime();
        String sqlQuery = "INSERT INTO feeds (user_id, timestamp, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, timestamp, eventType, operation, entityId);

    }

}