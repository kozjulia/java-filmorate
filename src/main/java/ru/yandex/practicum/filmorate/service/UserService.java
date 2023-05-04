package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addInFriends(User friendRequest, User friendResponse) {
        userStorage.addInFriends(friendRequest, friendResponse);
    }

    public void deleteFromFriends(User friendRequest, User friendResponse) {
        userStorage.deleteFromFriends(friendRequest, friendResponse);
    }

    public List<User> findMutualFriends(User friendRequest, User friendResponse) {
        return userStorage.findMutualFriends(friendRequest, friendResponse);
    }

}
