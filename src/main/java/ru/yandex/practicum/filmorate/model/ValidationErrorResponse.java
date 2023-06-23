package ru.yandex.practicum.filmorate.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    // доп. класс для корректного вывода ошибок валидации MethodArgumentNotValidException

    private final List<ErrorResponse> errorResponses;
}