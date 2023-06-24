package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> create(User user);

    Optional<User> update(User user);

    boolean delete(User user);

    boolean deleteUserById(long userId);

    List<User> findUsers();

    Optional<User> findUserById(long userId);

    boolean isFindUserById(long userId);

    List<Event> getUserEvent(Integer id);

}