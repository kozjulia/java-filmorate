package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public boolean delete(User user) {
        return userStorage.delete(user);
    }

    public List<User> findUsers() {
        return userStorage.findUsers();
    }

    public User findUserById(long userId) {
        return userStorage.findUserById(userId);
    }

    public boolean addInFriends(long id, long friendId) {
        if (userStorage.findUserById(id) == null || userStorage.findUserById(friendId) == null) {
            return false;
        }

        User friendRequest = userStorage.findUserById(id);
        User friendResponse = userStorage.findUserById(friendId);
        friendRequest.getFriends().add(friendId);
        friendResponse.getFriends().add(id);
        return true;
    }

    public boolean deleteFromFriends(long id, long friendId) {
        if (findUserById(id) == null || findUserById(friendId) == null) {
            return false;
        }

        User friendRequest = userStorage.findUserById(id);
        User friendResponse = userStorage.findUserById(friendId);
        friendRequest.getFriends().remove(friendId);
        friendResponse.getFriends().remove(id);
        return true;
    }

    public List<User> findFriends(long id) {
        if (findUserById(id) == null) {
            return Collections.EMPTY_LIST;
        }

        return findUserById(id).getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(long id, long otherId) {
        if (findUserById(id) == null || findUserById(otherId) == null) {
            return Collections.EMPTY_LIST;
        }

        return findFriends(id).stream()
                .filter(f -> findFriends(otherId).contains(f))
                .collect(Collectors.toList());
    }

}