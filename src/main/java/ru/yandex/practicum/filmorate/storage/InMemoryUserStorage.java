package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    public static long usersId = 0;  // сквозной счетчик пользователей

    @Override
    public User create(User user) {
        user = validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (findUserById(user.getId()) == null) {
            return null;
        }
        user = validate(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(User user) {
        long idUser = user.getId();
        if (users.containsKey(idUser)) {
            users.remove(idUser);
            return true;
        }
        return false;
    }

    @Override
    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(long userId) {
        if (users.get(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
        return users.get(userId);
    }

    private static Long getNextId() {
        return ++usersId;
    }

    private User validate(User user) {
        ValidatorControllers.validateEmail(user.getEmail());
        ValidatorControllers.validateLogin(user.getLogin());
        user = validateName(user);
        ValidatorControllers.validateBirthday(user.getBirthday());
        return user;
    }

    private User validateName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

}