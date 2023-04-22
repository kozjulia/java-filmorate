package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
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

    protected final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    @Validated
    // создание пользователя
    public User create(@Valid @RequestBody User user) {
        user = validate(user);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    @Validated
    // обновление пользователя
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            ValidatorControllers.logAndError("Ошибка! Невозможно обновить пользователя - его не существует.");
        }
        user = validate(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    // получение списка всех пользователей
    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    private User validate(User user) {
        ValidatorControllers.validateEmail(user.getEmail());
        ValidatorControllers.validateLogin(user.getLogin());
        user = validateName(user);
        ValidatorControllers.validateBirthday(user.getBirthday());
        user = validateId(user);
        return user;
    }

    private User validateId(User user) {
        if (user.getId() == 0) {
            user.setId(User.usersId++);
        }
        return user;
    }

    private User validateName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}