package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
public class Review {
    Long reviewId;
    @NotBlank(message = "Описание не может быть пустым.")
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
