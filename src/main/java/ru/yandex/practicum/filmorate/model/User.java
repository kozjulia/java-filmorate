package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class User {

    private long id; // целочисленный идентификатор
    @NonNull
    @NotBlank
    @Email
    private String email; // электронная почта
    @NonNull
    @NotBlank
    private String login; // логин пользователя
    private String name; // имя для отображения
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday; // дата рождения
    private Set<Long> friends = new HashSet<>(); // друзья

}