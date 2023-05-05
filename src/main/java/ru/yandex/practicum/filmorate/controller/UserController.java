package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Validated
    // создание пользователя
    public User create(@Valid @RequestBody User user) {
        userService.create(user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    @Validated
    // обновление пользователя
    public User update(@Valid @RequestBody User user) {
        userService.update(user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    @DeleteMapping
    @Validated
    // обновление фильма
    public void delete(@Valid @RequestBody User user) {
        userService.delete(user);
        log.debug("Удалён пользователь: {}", user);
    }

    @GetMapping
    // получение списка всех пользователей
    public List<User> findUsers() {
        log.debug("Получен список пользователей, количество = : {}", userService.findUsers().size());
        return userService.findUsers();
    }

    @GetMapping("/{userId}")
    // получение пользователя по id
    public User findUserById(@PathVariable long userId) {
        log.debug("Получен пользователь с id = : {}", userId);
        return userService.findUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    //  добавление в друзья
    public boolean addInFriends(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Пользователь c id = {} добавил в друзья пользователя с id = {}", id, friendId);
        return userService.addInFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    //  удаление из друзей
    public boolean deleteFromFriends(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Пользователь c id = {} удалил из друзей пользователя с id = {}", id, friendId);
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    // возвращаем список пользователей, являющихся его друзьями
    public List<User> findFriends(@PathVariable long id) {
        log.debug("Получен список пользователей, являющимися друзьями пользователя с id = {}", id);
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    // список друзей, общих с другим пользователем
    public List<User> findMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.debug("Получен список друзей пользователя с id = {}, общих с пользователем с id = {}", id, otherId);
        return userService.findMutualFriends(id, otherId);
    }

}