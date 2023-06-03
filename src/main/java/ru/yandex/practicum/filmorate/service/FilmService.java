package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingMPANotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Qualifier("likeDbStorage")
    private final LikeStorage likeStorage;

    public Film create(Film film) {
        return filmStorage.create(film).get();
    }

    public Film update(Film film) {
        if (findFilmById(film.getId()) == null) {
            return null;
        }
        return filmStorage.update(film).get();
    }

    public boolean delete(Film film) {
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
        if (findFilmById(id) == null || userStorage.findUserById(userId).isEmpty()) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().add(userId);
        likeStorage.dislike(film);
        likeStorage.like(film);
        return true;
    }

    public boolean dislike(long id, long userId) {
        if (findFilmById(id) == null || userStorage.findUserById(userId).isEmpty()) {
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

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

}