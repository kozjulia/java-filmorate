package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> findDirectors();

    Optional<Director> findDirectorById(long directorId);

    boolean isFindDirectorById(long directorId);

    Optional<Director> createDirector(Director director);

    Optional<Director> updateDirector(Director director);

    boolean deleteDirectorById(Long directorId);

}