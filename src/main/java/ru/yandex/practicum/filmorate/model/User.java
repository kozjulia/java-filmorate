package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

@Data
public class User {

    private long id; // целочисленный идентификатор
    private String name; // имя для отображения
    @NonNull
    @NotBlank
    @Email(message = "Ошибка! Неверный e-mail.")
    private String email; // электронная почта
    @NonNull
    @NotBlank(message = "Ошибка! Логин не может быть пустым.")
    private String login; // логин пользователя
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday; // дата рождения
    private Set<Long> friends = new HashSet<>(); // друзья

}