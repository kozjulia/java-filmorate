package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> create(Film film);

    Optional<Film> update(Film film);

    boolean delete(Film film);

    boolean deleteFilmById(long filmId);

    List<Film> findFilms();

    Optional<Film> findFilmById(long filmId);

    boolean isFindFilmById(long filmId);

    List<Genre> findGenres();

    Optional<Genre> findGenreById(long genreId);

    List<RatingMPA> findRatingMPAs();

    Optional<RatingMPA> findRatingMPAById(long ratingMPAId);

    List<Director> findDirectors();

    Optional<Director> findDirectorById(long directorId);

    boolean isFindDirectorById(long directorId);

    Optional<Director> createDirector(Director director);

    Optional<Director> updateDirector(Director director);

    boolean deleteDirectorById(Long directorId);

    List<Film> findSortFilmsByDirector(long directorId, String sortBy);

    List<Film> findSortFilmsBySubstring(String query, boolean isDirector, boolean isTitle);

    List<Film> findCommonSortFilms(long userId, long friendId);

}