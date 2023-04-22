package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    protected final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    @Validated
    // добавление фильма
    public Film create(@Valid @RequestBody Film film) {
        film = validate(film);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    @Validated
    // обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            ValidatorControllers.logAndError("Ошибка! Невозможно обновить фильм - его не существует.");
        }
        validate(film);
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    @GetMapping
    // получение всех фильмов
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    private Film validate(Film film) {
        ValidatorControllers.validateName(film.getName());
        ValidatorControllers.validateDescription(film.getDescription());
        ValidatorControllers.validateReleaseDate(film.getReleaseDate());
        ValidatorControllers.validateDuration(film.getDuration());
        film = validateId(film);
        return film;
    }

    private Film validateId(Film film) {
        if (film.getId() == 0) {
            film.setId(Film.filmsId++);
        }
        return film;
    }
}