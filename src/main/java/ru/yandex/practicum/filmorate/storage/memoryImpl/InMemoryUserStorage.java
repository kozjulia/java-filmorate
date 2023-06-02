package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    public static long usersId = 0;  // сквозной счетчик пользователей

    public Optional<User> create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    public Optional<User> update(User user) {
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    public boolean delete(User user) {
        long idUser = user.getId();
        if (users.containsKey(idUser)) {
            users.remove(idUser);
            return true;
        }
        return false;
    }

    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findUserById(long userId) {
        if (users.get(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
        if (users.get(userId) == null) {
            return Optional.empty();
        } else {
            return Optional.of(users.get(userId));
        }
    }

    private static Long getNextId() {
        return ++usersId;
    }

}