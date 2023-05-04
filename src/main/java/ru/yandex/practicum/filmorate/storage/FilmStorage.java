package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    boolean delete(Film film);

    List<Film> findFilms();

    void like(Film film, User user);

    void dislike(Film film, User user);

    List<Film> findPopularFilms();
}
