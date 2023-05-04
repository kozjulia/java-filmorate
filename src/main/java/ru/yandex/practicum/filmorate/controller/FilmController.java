package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
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

    //protected final Map<Integer, Film> films = new HashMap<>();
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @Validated
    // добавление фильма
    public Film create(@Valid @RequestBody Film film) {



        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    @Validated
    // обновление фильма
    public Film update(@Valid @RequestBody Film film) {




        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    @DeleteMapping
    @Validated
    // обновление фильма
    public boolean delete(@Valid @RequestBody Film film) {




        log.debug("Удалён фильм: {}", film);
        return false;
    }

    @GetMapping
    // получение всех фильмов
    public List<Film> findFilms() {



        return null;
    }



}