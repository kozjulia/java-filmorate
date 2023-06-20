package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
public class Review {
    Long reviewId;
    @NonNull
    @Size(max = 1000, message = "Описание не может быть больше 1000 символов.")
    String content;
    @NonNull
    Boolean isPositive;
    @NonNull
    Long userId;
    @NonNull
    Long filmId;
    Integer useful;
}
