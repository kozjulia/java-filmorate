package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class ValidatorControllers {
    // валидация для контроллеров

    public static void validateName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            logAndError("Ошибка! Название не может быть пустым.");
        }
    }

    public static void validateDescription(String description) {
        if (description.length() > 200) {
            logAndError("Ошибка! Максимальная длина описания — 200 символов.");
        }
    }

    public static void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            logAndError("Ошибка! Дата релиза — не раньше 28 декабря 1895 года.");
        }
    }

    public static void validateDuration(int duration) {
        if (duration < 0) {
            logAndError("Ошибка! Продолжительность фильма должна быть положительной.");
        }
    }

    public static void validateEmail(String email) {
        if (email.isEmpty() || email.isBlank() || !email.contains("@")) {
            logAndError("Ошибка! Неверный e-mail.");
        }
    }

    public static void validateLogin(String login) {
        if (login.isEmpty() || login.isBlank() || login.contains(" ")) {
            logAndError("Ошибка! Логин не может быть пустым и содержать пробелы.");
        }
    }

    public static void validateBirthday(LocalDate birthday) {
        if (birthday.isAfter(LocalDate.now())) {
            logAndError("Ошибка! Дата рождения не может быть в будущем.");
        }
    }

    public static void logAndError(String exp) {
        log.warn(exp);
        throw new ValidationException(exp);
    }

}