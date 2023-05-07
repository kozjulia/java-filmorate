package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public boolean delete(Film film) {
        return filmStorage.delete(film);
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms();
    }

    public Film findFilmById(long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public boolean like(long id, long userId) {
        if (findFilmById(id) == null || userStorage.findUserById(userId) == null) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().add(userId);
        return true;
    }

    public boolean dislike(long id, long userId) {
        if (findFilmById(id) == null || userStorage.findUserById(userId) == null) {
            return false;
        }

        Film film = findFilmById(id);
        film.getLikes().remove(userId);
        return true;
    }

    public List<Film> findPopularFilms(int count) {
        return findFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

}