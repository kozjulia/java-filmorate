package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mark;
import ru.yandex.practicum.filmorate.storage.MarkStorage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryMarkStorage implements MarkStorage {

    public boolean rate(long id, long userId, int grade) {
        return false;
    }

    public boolean unrate(long id, long userId) {
        return false;
    }

    public List<Long> findLikes(Film film) {
        return Collections.EMPTY_LIST;
    }

    public List<Mark> findMarks(Film film) {
        return Collections.EMPTY_LIST;
    }

    public Map<Long, Set<Long>> findAllUsersWithPositiveMarks() {
        return Collections.EMPTY_MAP;
    }

}