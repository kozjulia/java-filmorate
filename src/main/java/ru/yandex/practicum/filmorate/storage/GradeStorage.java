package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Grade;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GradeStorage {

    boolean rate(long id, long userId, int grade);

    boolean unrate(long id, long userId);

    List<Grade> findGrades(Film film);

    Map<Long, Set<Long>> findAllUsersWithPositiveGrades();

}