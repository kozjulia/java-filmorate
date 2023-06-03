package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class ValidatorControllers {
    // валидация для фильмов и юзеров

    public static Film validateFilm(Film film) {
        ValidatorControllers.validateName(film.getName());
        ValidatorControllers.validateDescription(film.getDescription());
        ValidatorControllers.validateReleaseDate(film.getReleaseDate());
        ValidatorControllers.validateDuration(film.getDuration());
        return film;
    }

    private static void validateName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            logAndError("Ошибка! Название не может быть пустым.");
        }
    }

    private static void validateDescription(String description) {
        if (description.length() > 200) {
            logAndError("Ошибка! Максимальная длина описания — 200 символов.");
        }
    }

    private static void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            logAndError("Ошибка! Дата релиза — не раньше 28 декабря 1895 года.");
        }
    }

    private static void validateDuration(int duration) {
        if (duration < 0) {
            logAndError("Ошибка! Продолжительность фильма должна быть положительной.");
        }
    }

    public static User validateUser(User user) {
        ValidatorControllers.validateEmail(user.getEmail());
        ValidatorControllers.validateLogin(user.getLogin());
        user = validateUserName(user);
        ValidatorControllers.validateBirthday(user.getBirthday());
        return user;
    }

    private static User validateUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private static void validateEmail(String email) {
        if (email.isEmpty() || email.isBlank() || !email.contains("@")) {
            logAndError("Ошибка! Неверный e-mail.");
        }
    }

    private static void validateLogin(String login) {
        if (login.isEmpty() || login.isBlank() || login.contains(" ")) {
            logAndError("Ошибка! Логин не может быть пустым и содержать пробелы.");
        }
    }

    private static void validateBirthday(LocalDate birthday) {
        if (birthday.isAfter(LocalDate.now())) {
            logAndError("Ошибка! Дата рождения не может быть в будущем.");
        }
    }

    private static void logAndError(String exp) {
        log.warn(exp);
        throw new ValidationException(exp);
    }

}