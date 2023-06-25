package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Grade;
import ru.yandex.practicum.filmorate.storage.GradeStorage;

import java.util.Collections;
import java.util.List;

public class InMemoryGradeStorage implements GradeStorage {

    public boolean rate(long id, long userId, int grade) {
        return false;
    }

    public boolean unrate(long id, long userId) {
        return false;
    }

    public List<Grade> findGrades(Film film) {
        return Collections.EMPTY_LIST;
    }

}