package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    // доп. класс для корректного вывода ошибок валидации MethodArgumentNotValidException

    private final List<ErrorResponse> errorResponses;
}