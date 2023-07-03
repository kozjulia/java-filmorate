package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingMPANotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Qualifier("markDbStorage")
    private final MarkStorage markStorage;
    @Qualifier("eventDbStorage")
    private final EventStorage eventStorage;

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

    public boolean deleteFilmById(long filmId) {
        if (!filmStorage.isFindFilmById(filmId)) {
            return false;
        }
        return filmStorage.deleteFilmById(filmId);
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms().stream()
                .peek(film -> film.setLikes(new HashSet<>(markStorage.findLikes(film))))
                .peek(film -> film.setMarks(new HashSet<>(markStorage.findMarks(film))))
                .collect(Collectors.toList());
    }

    public Film findFilmById(long filmId) {
        return filmStorage.findFilmById(filmId).stream()
                .peek(f -> f.setLikes(new HashSet<>(markStorage.findLikes(f))))
                .peek(film -> film.setMarks(new HashSet<>(markStorage.findMarks(film))))
                .findFirst().get();
    }

    public boolean like(long id, long userId, Integer mark) {
        if ((mark < 1) || (mark > 10)) {
            String message = "Параметр mark должен быть от 1 до 10 включительно!";
            log.warn(message);
            throw new ValidationException(message, 10001);
        }
        if (!filmStorage.isFindFilmById(id) || !userStorage.isFindUserById(userId)) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().add(userId);
        markStorage.rate(id, userId, mark);
        eventStorage.createEvent(userId, "LIKE", "ADD", id);
        return true;
    }

    public boolean dislike(long id, long userId) {
        if (!filmStorage.isFindFilmById(id) || !userStorage.isFindUserById(userId)) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().remove(userId);
        markStorage.unrate(id, userId);
        eventStorage.createEvent(userId, "LIKE", "REMOVE", id);
        return true;
    }

    public List<Film> findPopularFilms(int count, Long genreId, Integer year) {
        if (count < 0) {
            String message = "Параметр count не может быть отрицательным!";
            log.warn(message);
            throw new ValidationException(message, 10002);
        }
        if ((year != null) && (year < 0)) {
            String message = "Параметр year не может быть отрицательным!";
            log.warn(message);
            throw new ValidationException(message, 10003);
        }

        List<Film> result = findFilms().stream().sorted(this::compareMark).collect(Collectors.toList());
        if (genreId != null) {
            Genre genre = filmStorage.findGenreById(genreId).orElse(null);
            result = result.stream().filter(f -> f.getGenres().contains(genre)).collect(
                    Collectors.toList());
        }
        if (year != null) {
            result = result.stream().filter(f -> f.getReleaseDate().getYear() == year)
                    .collect(Collectors.toList());
        }

        return result.stream()
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
        return filmStorage.findDirectors();
    }

    public Director findDirectorById(long directorId) {
        return filmStorage.findDirectorById(directorId)
                .orElseThrow(() -> {
                    log.warn("Режиссёр № {} не найден", directorId);
                    throw new DirectorNotFoundException(String.format("Режиссёр № %d не найден", directorId));
                });
    }

    public Director createDirector(Director director) {
        return filmStorage.createDirector(director).get();
    }

    public Director updateDirector(Director director) {
        if (!filmStorage.isFindDirectorById(director.getId())) {
            return null;
        }
        return filmStorage.updateDirector(director).get();
    }

    public boolean deleteDirectorById(Long directorId) {
        if (!filmStorage.isFindDirectorById(directorId)) {
            return false;
        }
        return filmStorage.deleteDirectorById(directorId);
    }

    public List<Film> findSortFilmsByDirector(long directorId, String sortBy) {
        if (!sortBy.equals("year") && !sortBy.equals("likes")) {
            String message = "Параметр sortBy может быть только year или likes!";
            log.warn(message);
            throw new ValidationException(message, 10004);
        }
        if (!filmStorage.isFindDirectorById(directorId)) {
            return Collections.EMPTY_LIST;
        }
        return filmStorage.findSortFilmsByDirector(directorId, sortBy);
    }

    public List<Film> findSortFilmsBySubstring(String query, String by) {
        if (!by.contains("director") && !by.contains("title")) {
            String message = "Параметр by должен содержать director или/и title!";
            log.warn(message);
            throw new ValidationException(message, 10005);
        }
        boolean isDirector = by.contains("director");
        boolean isTitle = by.contains("title");
        return filmStorage.findSortFilmsBySubstring(query, isDirector, isTitle);
    }

    public List<Film> findCommonSortFilms(long userId, long friendId) {
        return filmStorage.findCommonSortFilms(userId, friendId);
    }

    private int compareMark(Film film1, Film film2) {
        return (int) ((film2.getMarks().stream()
                .mapToInt(Mark::getMark)
                .summaryStatistics()
                .getAverage()
                - film1.getMarks().stream()
                .mapToInt(Mark::getMark)
                .summaryStatistics()
                .getAverage()) * 100);
    }

}