package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InMemoryDirectorStorage implements DirectorStorage {

    public List<Director> findDirectors() {
        return Collections.EMPTY_LIST;
    }

    public Optional<Director> findDirectorById(long directorId) {
        return Optional.empty();
    }

    public boolean isFindDirectorById(long directorId) {
        return false;
    }

    public Optional<Director> createDirector(Director director) {
        return Optional.empty();
    }

    public Optional<Director> updateDirector(Director director) {
        return Optional.empty();
    }

    public boolean deleteDirectorById(Long directorId) {
        return false;
    }

}