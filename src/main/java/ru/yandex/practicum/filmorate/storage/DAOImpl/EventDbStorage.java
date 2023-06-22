package ru.yandex.practicum.filmorate.storage.DAOImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.sql.Timestamp;
import java.time.Instant;

@Component("eventDbStorage")
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private int eventId = 0;

    @Override
    public void createEvent(long userId, String eventType, String operation, long entityId) {
        long timestamp = Timestamp.from(Instant.now()).getTime();
        eventId += 1;
        String sqlQuery = "INSERT INTO feeds (userId, timestamp, eventType, operation, entityId, eventId) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, timestamp, eventType, operation, entityId, eventId);

    }
}
