package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class RatingMPA {
    // Рейтинг Ассоциации кинокомпаний (МРА)
    // Эта оценка определяет возрастное ограничение для фильма
    // G - у фильма нет возрастных ограничений
    // PG - детям рекомендуется смотреть фильм с родителями
    // PG-13 - детям до 13 лет просмотр не желателен
    // R - лицам до 17 лет просматривать фильм можно только в присутствии взрослого
    // NC-17 - лицам до 18 лет просмотр запрещён
    @NonNull
    private final Long id; // целочисленный идентификатор
    @NotBlank(message = "Ошибка! Название не может быть пустым.")
    private final String name; // название
    @NotBlank(message = "Ошибка! Описание не может быть пустым.")
    private final String description; // описание

}