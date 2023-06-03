package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class Film {

    private long id; // целочисленный идентификатор
    @NonNull
    @NotBlank(message = "Ошибка! Название не может быть пустым.")
    private String name; // название
    @NonNull
    private String description; // описание
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate; // дата релиза
    @Positive(message = "Ошибка! Продолжительность фильма должна быть положительной.")
    private int duration; // продолжительность
    private int rate; // рейтинг
    private Set<Long> likes = new HashSet<>(); // пользователи, лайкнувшие фильм
    private Set<Genre> genres = new HashSet<>(); // жанры
    private RatingMPA mpa; // рейтинг Ассоциации кинокомпаний (МРА)

}