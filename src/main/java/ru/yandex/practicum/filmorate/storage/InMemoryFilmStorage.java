package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    protected final Map<Long, Film> films = new HashMap<>();
    private static long filmsId = 1;  // сквозной счетчик

    @Override
    public Film create(Film film) {
        film = validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            ValidatorControllers.logAndError("Ошибка! Невозможно обновить фильм - его не существует.");
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
    public void like(Film film, User user) {
        Set<Long> Likes = new HashSet<>();
        Likes.addAll(films.get(film.getId()).getLikes());
        Likes.add(user.getId());
        film.setLikes(Likes);
        films.put(film.getId(), film);
    }

    @Override
    public void dislike(Film film, User user) {
        Set<Long> Likes = new HashSet<>();
        Likes.addAll(films.get(film.getId()).getLikes());
        Likes.remove(user.getId());
        film.setLikes(Likes);
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> findPopularFilms() {
        return films.values().stream()
                .sorted((f1, f2) -> compare(f1, f2))
                .limit(10)
                .map(i -> films.get(i))
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    private static Long getNextId() {
        return filmsId++;
    }

    private Film validate(Film film) {
        ValidatorControllers.validateName(film.getName());
        ValidatorControllers.validateDescription(film.getDescription());
        ValidatorControllers.validateReleaseDate(film.getReleaseDate());
        ValidatorControllers.validateDuration(film.getDuration());
        //film = validateId(film);
        return film;
    }

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

   /* private Film validateId(Film film) {
        if (film.getId() == 0) {
            film.setId(Film.filmsId++);
        }
        return film;
    }*/

}
