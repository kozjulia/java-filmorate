package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LikeStorage {

    boolean like(Film film);

    boolean dislike(Film film);

    List<Long> findLikes(Film film);

    Map<Long, Set<Long>> findAllUsersWithLikes();

}