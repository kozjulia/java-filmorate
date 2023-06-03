package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {

    boolean like(Film film);

    boolean dislike(Film film);

    List<Long> findLikes(Film film);

}