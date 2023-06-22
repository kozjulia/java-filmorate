package ru.yandex.practicum.filmorate.storage;

public interface EventStorage {

    void createEvent(long userId, String eventType, String operation, long entityId);
}
