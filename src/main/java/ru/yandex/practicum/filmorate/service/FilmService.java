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
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public Optional<Film> create(Film film) {
        return filmStorage.create(film);
    }

    public Optional<Film> update(Film film) {
        return filmStorage.update(film);
    }

    public boolean delete(Film film) {
        return filmStorage.delete(film);
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms().stream()
                .peek(film -> film.setLikes(new HashSet<>(likeStorage.findLikes(film))))
                .collect(Collectors.toList());
    }

    public Optional<Film> findFilmById(long filmId) {
        return filmStorage.findFilmById(filmId).stream()
                .peek(f -> f.setLikes(new HashSet<>(likeStorage.findLikes(f))))
                .findFirst();
    }

    public boolean like(long id, long userId) {
        if (findFilmById(id).isEmpty() || userStorage.findUserById(userId).isEmpty()) {
            return false;
        }

        Film film = findFilmById(id).get();
        film.getLikes().add(userId);
        likeStorage.dislike(film);
        likeStorage.like(film);
        return true;
    }

    public boolean dislike(long id, long userId) {
        if (findFilmById(id).isEmpty() || userStorage.findUserById(userId).isEmpty()) {
            return false;
        }

        Film film = findFilmById(id).get();
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

    public Optional<Genre> findGenreById(long genreId) {
        Optional<Genre> genre = filmStorage.findGenreById(genreId);
        if (genre.isEmpty()) {
            log.error("Жанр № {} не найден", genreId);
            throw new GenreNotFoundException(String.format("Жанр № %d не найден", genreId));
        } else {
            return genre;
        }
    }

    public List<RatingMPA> findRatingMPAs() {
        return filmStorage.findRatingMPAs();
    }

    public Optional<RatingMPA> findRatingMPAById(long ratingMPAId) {
        Optional<RatingMPA> ratingMPA = filmStorage.findRatingMPAById(ratingMPAId);
        if (ratingMPA.isEmpty()) {
            log.error("Рейтинг МПА № {} не найден", ratingMPAId);
            throw new RatingMPANotFoundException(String.format("Рейтинг МПА № %d не найден", ratingMPAId));
        } else {
            return ratingMPA;
        }
    }

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

}