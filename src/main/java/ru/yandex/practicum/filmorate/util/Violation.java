package ru.yandex.practicum.filmorate.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Violation {
    // доп. класс для корректного вывода ошибок валидации MethodArgumentNotValidException
    private final String fieldName;
    private final String message;
}