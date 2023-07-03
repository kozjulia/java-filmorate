package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class Mark {

    @NonNull
    private final Long userId; // идентификатор пользователя, оставившего оценку
    @NonNull
    @Min(value = 1, message = "Ошибка! Оценка не может быть меньше 1.")
    @Max(value = 10, message = "Ошибка! Оценка не может быть больше 10.")
    private Integer mark; // оценка от 1 до 10 включительно

}