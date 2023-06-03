package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friends {

    private Long requestFriend; // пользователь, который отправил запрос на добавление другого пользователя в друзья
    private Long responseFriend; // пользователь, которому отправили запрос на добавление в друзья

}