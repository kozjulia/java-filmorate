package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    public static long filmsId = 0;  // сквозной счетчик фильмов

    public Optional<Film> create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    public Optional<Film> update(Film film) {
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    public boolean delete(Film film) {
        long idFilm = film.getId();
        if (films.containsKey(idFilm)) {
            films.remove(idFilm);
            return true;
        }
        return false;
    }

    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    public Optional<Film> findFilmById(long filmId) {
        if (films.get(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
        if (films.get(filmId) == null) {
            return Optional.empty();
        } else {
            return Optional.of(films.get(filmId));
        }
    }

    public List<Genre> findGenres() {
        return Collections.EMPTY_LIST;
    }

    public Optional<Genre> findGenreById(long genreId) {
        return Optional.empty();
    }

    public List<RatingMPA> findRatingMPAs() {
        return Collections.EMPTY_LIST;
    }

    public Optional<RatingMPA> findRatingMPAById(long ratingMPAId) {
        return Optional.empty();
    }

    private static Long getNextId() {
        return ++filmsId;
    }

}