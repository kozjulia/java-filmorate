package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class User {

    private int id; // целочисленный идентификатор
    @NonNull
    @NotBlank
    @Email
    //(message = "Электронный адрес невалидный: ${validatedValue}",
    //regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email; // электронная почта
    @NonNull
    @NotBlank
    private String login; // логин пользователя
    private String name; // имя для отображения
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday; // дата рождения

    /* С этой аннотацией Valid ничего не получается. в тестах пытаюсь электронный адрес сохранить кириллицу
     * должна ошибка вылетать - но не вылетает. поэтому остальные аннотации на длину строки и тд не ввожу
     * помоги пожалуйста. гугл, что нашла - не помогло*/

    public static int usersId = 1; // сквозной счетчик

    public User(@NonNull @NotBlank @Email String email, @NonNull @NotBlank String login,
                @NonNull LocalDate birthday) {
        this.id = usersId++;
        this.email = email;
        this.login = login;
        this.name = login;
        this.birthday = birthday;
    }

    public User(@NonNull @NotBlank @Email String email, @NonNull @NotBlank String login,
                String name, @NonNull LocalDate birthday) {
        this.id = usersId++;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}