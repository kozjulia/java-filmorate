package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class Review {

    private Long reviewId;
    @NotBlank(message = "Описание не может быть пустым.")
    @Size(max = 1000, message = "Описание не может быть больше 1000 символов.")
    private String content;
    @NonNull
    private Boolean isPositive;
    @NonNull
    private Long userId;
    @NonNull
    private Long filmId;
    private Integer useful;

}