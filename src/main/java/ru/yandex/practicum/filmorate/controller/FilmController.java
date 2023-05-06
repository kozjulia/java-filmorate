package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @Validated
    // добавление фильма
    public Film create(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    @Validated
    // обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        filmService.update(film);
        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    @DeleteMapping
    @Validated
    // удаление фильма
    public void delete(@Valid @RequestBody Film film) {
        filmService.delete(film);
        log.debug("Удалён фильм: {}", film);
    }

    @GetMapping
    // получение всех фильмов
    public List<Film> findFilms() {
        log.debug("Получен список фильмов, количество = : {}", filmService.findFilms().size());
        return filmService.findFilms();
    }

    @GetMapping("/{filmId}")
    // получение пользователя по id
    public Film findUserById(@PathVariable long filmId) {
        log.debug("Получен фильм с id = : {}", filmId);
        return filmService.findFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    //  пользователь ставит лайк фильму
    public boolean like(@PathVariable long id, @PathVariable long userId) {
        log.debug("Пользователь id = {} лайкнул фильм id = {}", userId, id);
        return filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    //  пользователь удаляет лайк
    public boolean dislike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Пользователь id = {} удалил лайк с фильма id = {}", userId, id);
        return filmService.dislike(id, userId);
    }

    @GetMapping("/popular")
    //  возвращает список из первых count фильмов по количеству лайков.
    //  Если значение параметра count не задано, возвращает первые 10
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10") String count) {
        int countInt = Integer.parseInt(count);
        if (countInt < 0) {
            throw new ValidationException("Параметр count не может быть отрицательным!");
        }
        log.debug("Получен список из первых {} фильмов по количеству лайков", countInt);
        return filmService.findPopularFilms(countInt);
    }

}