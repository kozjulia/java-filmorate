package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> create(Film film);

    Optional<Film> update(Film film);

    boolean delete(Film film);

    List<Film> findFilms();

    Optional<Film> findFilmById(long filmId);

    List<Genre> findGenres();

    Optional<Genre> findGenreById(long genreId);

    List<RatingMPA> findRatingMPAs();

    Optional<RatingMPA> findRatingMPAById(long ratingMPAId);

}