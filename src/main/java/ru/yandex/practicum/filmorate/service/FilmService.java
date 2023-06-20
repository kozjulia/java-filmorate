package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingMPANotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("directorDbStorage")
    private final DirectorStorage directorStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Qualifier("likeDbStorage")
    private final LikeStorage likeStorage;

    public Film create(Film film) {
        return filmStorage.create(film).get();
    }

    public Film update(Film film) {
        if (!filmStorage.isFindFilmById(film.getId())) {
            return null;
        }
        return filmStorage.update(film).get();
    }

    public boolean delete(Film film) {
        if (!filmStorage.isFindFilmById(film.getId())) {
            return false;
        }
        return filmStorage.delete(film);
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms().stream()
                .peek(film -> film.setLikes(new HashSet<>(likeStorage.findLikes(film))))
                .collect(Collectors.toList());
    }

    public Film findFilmById(long filmId) {
        return filmStorage.findFilmById(filmId).stream()
                .peek(f -> f.setLikes(new HashSet<>(likeStorage.findLikes(f))))
                .findFirst().get();
    }

    public boolean like(long id, long userId) {
        if (!filmStorage.isFindFilmById(id) || !userStorage.isFindUserById(userId)) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().add(userId);
        likeStorage.dislike(film);
        likeStorage.like(film);
        return true;
    }

    public boolean dislike(long id, long userId) {
        if (!filmStorage.isFindFilmById(id) || !userStorage.isFindUserById(userId)) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().remove(userId);
        likeStorage.dislike(film);
        likeStorage.like(film);
        return true;
    }

    public List<Film> findPopularFilms(int count) {
        return findFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Genre> findGenres() {
        return filmStorage.findGenres();
    }

    public Genre findGenreById(long genreId) {
        return filmStorage.findGenreById(genreId)
                .orElseThrow(() -> {
                    log.warn("Жанр № {} не найден", genreId);
                    throw new GenreNotFoundException(String.format("Жанр № %d не найден", genreId));
                });
    }

    public List<RatingMPA> findRatingMPAs() {
        return filmStorage.findRatingMPAs();
    }

    public RatingMPA findRatingMPAById(long ratingMPAId) {
        return filmStorage.findRatingMPAById(ratingMPAId)
                .orElseThrow(() -> {
                    log.warn("Рейтинг МПА № {} не найден", ratingMPAId);
                    throw new RatingMPANotFoundException(String.format("Рейтинг МПА № %d не найден", ratingMPAId));
                });
    }

    public List<Director> findDirectors() {
        return directorStorage.findDirectors();
    }

    public Director findDirectorById(long directorId) {
        return directorStorage.findDirectorById(directorId)
                .orElseThrow(() -> {
                    log.warn("Режиссёр № {} не найден", directorId);
                    throw new DirectorNotFoundException(String.format("Режиссёр № %d не найден", directorId));
                });
    }

    public Director createDirector(Director director) {
        return directorStorage.createDirector(director).get();
    }

    public Director updateDirector(Director director) {
        if (!directorStorage.isFindDirectorById(director.getId())) {
            return null;
        }
        return directorStorage.updateDirector(director).get();
    }

    public boolean deleteDirectorById(Long directorId) {
        if (!directorStorage.isFindDirectorById(directorId)) {
            return false;
        }
        return directorStorage.deleteDirectorById(directorId);
    }

    public List<Film> findSortFilmsByDirector(long directorId, String sortBy) {
        if (!directorStorage.isFindDirectorById(directorId)) {
            return Collections.EMPTY_LIST;
        }
        return filmStorage.findSortFilmsByDirector(directorId, sortBy);
    }

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

}