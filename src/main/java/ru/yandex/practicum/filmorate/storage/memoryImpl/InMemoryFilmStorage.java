package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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

    public boolean deleteFilmById(long filmId) {
        if (films.containsKey(filmId)) {
            films.remove(filmId);
            return true;
        }
        return false;
    }

    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    public Optional<Film> findFilmById(long filmId) {
        if (films.get(filmId) == null) {
            log.warn("Фильм № {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
        return Optional.of(films.get(filmId));
    }

    public boolean isFindFilmById(long filmId) {
        if (films.get(filmId) == null) {
            log.warn("Фильм № {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
        return true;
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

    public List<Director> findDirectors() {
        return Collections.EMPTY_LIST;
    }

    public Optional<Director> findDirectorById(long directorId) {
        return Optional.empty();
    }

    public boolean isFindDirectorById(long directorId) {
        return false;
    }

    public Optional<Director> createDirector(Director director) {
        return Optional.empty();
    }

    public Optional<Director> updateDirector(Director director) {
        return Optional.empty();
    }

    public boolean deleteDirectorById(Long directorId) {
        return false;
    }

    public List<Film> findSortFilmsByDirector(long directorId, String sortBy) {
        return Collections.EMPTY_LIST;
    }

    public List<Film> findSortFilmsBySubstring(String query, boolean isDirector, boolean isTitle) {
        return Collections.EMPTY_LIST;
    }

    public List<Film> findCommonSortFilms(long userId, long friendId) {
        return Collections.EMPTY_LIST;
    }

    private static Long getNextId() {
        return ++filmsId;
    }

}