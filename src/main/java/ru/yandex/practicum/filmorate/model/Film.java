package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private int id; // целочисленный идентификатор
    @NonNull
    @NotBlank
    private String name; // название
    @NonNull
    private String description; // описание
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate; // дата релиза
    private int duration; // продолжительность
    private int rate; // рейтинг

    public static int filmsId = 1; // сквозной счетчик

    public Film(@NonNull @NotBlank String name, @NonNull String description,
                @NonNull LocalDate releaseDate, int duration) {
        this.id = filmsId++;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(int id, @NonNull @NotBlank String name, @NonNull String description,
                @NonNull LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}