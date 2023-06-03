package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class Genre {
    // жанры фильмов, могут быть несколько у одного

    @NonNull
    private final Long id; // целочисленный идентификатор
    @NotBlank(message = "Ошибка! Название не может быть пустым.")
    private final String name; // название

}