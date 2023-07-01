package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;

import javax.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Validated
    /**
     * создание пользователя
     */
    public User create(@Valid @RequestBody User user) {
        user = ValidatorControllers.validateUser(user);
        User newUser = userService.create(user);
        log.debug("Добавлен новый пользователь: {}", newUser);
        return newUser;
    }

    @PutMapping
    @Validated
    /**
     * обновление пользователя
     */
    public User update(@Valid @RequestBody User user) {
        user = ValidatorControllers.validateUser(user);
        User newUser = userService.update(user);
        log.debug("Обновлен пользователь: {}", newUser);
        return newUser;
    }

    @DeleteMapping
    @Validated
    /**
     * удаление пользователя
     */
    public void delete(@Valid @RequestBody User user) {
        userService.delete(user);
        log.debug("Удалён пользователь: {}", user);
    }

    @DeleteMapping("/{userId}")
    @Validated
    /**
     * удаление пользователя по id
     */
    public void deleteUserById(@Valid @PathVariable long userId) {
        userService.deleteUserById(userId);
        log.debug("Удалён пользователь c id: {}", userId);
    }

    @GetMapping
    /**
     * получение списка всех пользователей
     */
    public List<User> findUsers() {
        List<User> users = userService.findUsers();
        log.debug("Получен список пользователей, количество = {}", users.size());
        return users;
    }

    @GetMapping("/{userId}")
    /**
     * получение пользователя по id
     */
    public User findUserById(@PathVariable long userId) {
        User user = userService.findUserById(userId);
        log.debug("Получен пользователь с id = {}", userId);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    /**
     * добавление в друзья
     */
    public boolean addInFriends(@PathVariable long id, @PathVariable long friendId) {
        if (userService.addInFriends(id, friendId)) {
            log.debug("Пользователь c id = {} добавил в друзья пользователя с id = {}", id, friendId);
            return true;
        }
        return false;
    }

    @DeleteMapping("{id}/friends/{friendId}")
    /**
     * удаление из друзей
     */
    public boolean deleteFromFriends(@PathVariable long id, @PathVariable long friendId) {
        if (userService.deleteFromFriends(id, friendId)) {
            log.debug("Пользователь c id = {} удалил из друзей пользователя с id = {}", id, friendId);
            return true;
        }
        return false;
    }

    @GetMapping("/{id}/friends")
    /**
     * получение списка пользователей, являющихся его друзьями
     */
    public List<User> findFriends(@PathVariable long id) {
        List<User> users = userService.findFriends(id);
        log.debug("Получен список пользователей, являющимися друзьями пользователя с id = {}, " +
                "количество = {}", id, users.size());
        return users;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    /**
     * получение списка друзей, общих с другим пользователем
     */
    public List<User> findMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        List<User> users = userService.findMutualFriends(id, otherId);
        log.debug("Получен список друзей пользователя с id = {}, общих с пользователем с id = {}, " +
                "количество = {}", id, otherId, users.size());
        return users;
    }

    @GetMapping("/{id}/recommendations")
    /**
     * получение списка рекомендуемых фильмов для пользователя
     */
    public List<Film> getRecommendations(@PathVariable Long id) {
        List<Film> recommendedFilms = userService.getRecommendations(id);
        log.debug("Получен список рекомендуемых фильмов для пользователя с id = {}, " +
                "количество = {}", id, recommendedFilms.size());
        return recommendedFilms;
    }

    @GetMapping("/{id}/feed")
    /**
     * получение новостной ленты пользователя
     */
    public List<Event> getEvent(@PathVariable int id) {
        List<Event> feeds = userService.getUserEvent(id);
        log.debug("Получена новостная лента пользователя - {}, количество записей {}",
                userService.findUserById(id).getName(), feeds.size());
        return feeds;
    }

}