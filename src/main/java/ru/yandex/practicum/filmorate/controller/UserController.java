package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    //protected final Map<Integer, User> users = new HashMap<>();

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Validated
    // создание пользователя
    public User create(@Valid @RequestBody User user) {




        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    @Validated
    // обновление пользователя
    public User update(@Valid @RequestBody User user) {




        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    @DeleteMapping
    @Validated
    // обновление фильма
    public boolean delete(@Valid @RequestBody User user) {




        log.debug("Удалён пользователь: {}", user);
        return false;
    }

    @GetMapping
    // получение списка всех пользователей
    public List<User> findUsers() {


        return null;
    }


}