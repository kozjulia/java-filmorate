package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    boolean delete(Film film);

    List<Film> findFilms();

    Film findFilmById(long filmId);

}