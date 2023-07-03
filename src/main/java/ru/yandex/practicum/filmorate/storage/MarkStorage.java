package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mark;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MarkStorage {

    boolean rate(long id, long userId, int grade);

    boolean unrate(long id, long userId);

    List<Long> findLikes(Film film);

    List<Mark> findMarks(Film film);

    Map<Long, Set<Long>> findAllUsersWithPositiveMarks();

}