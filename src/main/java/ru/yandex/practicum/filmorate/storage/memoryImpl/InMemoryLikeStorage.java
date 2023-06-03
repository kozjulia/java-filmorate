package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class InMemoryLikeStorage implements LikeStorage {

    public boolean like(Film film) {
        return false;
    }

    public boolean dislike(Film film) {
        return false;
    }

    public List<Long> findLikes(Film film) {
        return Collections.EMPTY_LIST;
    }

}