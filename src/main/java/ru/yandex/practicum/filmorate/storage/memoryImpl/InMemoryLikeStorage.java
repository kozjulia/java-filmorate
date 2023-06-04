package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InMemoryLikeStorage implements LikeStorage {

    private final InMemoryFilmStorage filmStorage;

    public boolean like(Film film) {
        return true;
    }

    public boolean dislike(Film film) {
        return true;
    }

    public List<Long> findLikes(Film film) {
        return new ArrayList<>(filmStorage.findFilmById(film.getId()).get().getLikes());
    }

}