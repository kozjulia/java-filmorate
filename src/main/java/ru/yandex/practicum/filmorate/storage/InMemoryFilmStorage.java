package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    public static long filmsId = 0;  // сквозной счетчик фильмов

    @Override
    public Film create(Film film) {
        film = validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (findFilmById(film.getId()) == null) {
            return null;
        }

        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean delete(Film film) {
        long idFilm = film.getId();
        if (films.containsKey(idFilm)) {
            films.remove(idFilm);
            return true;
        }

        return false;
    }

    @Override
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(long filmId) {
        if (films.get(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
        return films.get(filmId);
    }

    private static Long getNextId() {
        return ++filmsId;
    }

    private Film validate(Film film) {
        ValidatorControllers.validateName(film.getName());
        ValidatorControllers.validateDescription(film.getDescription());
        ValidatorControllers.validateReleaseDate(film.getReleaseDate());
        ValidatorControllers.validateDuration(film.getDuration());
        return film;
    }

}