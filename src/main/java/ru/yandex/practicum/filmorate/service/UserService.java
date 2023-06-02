package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public Optional<User> create(User user) {
        return userStorage.create(user);
    }

    public Optional<User> update(User user) {
        return userStorage.update(user);
    }

    public boolean delete(User user) {
        return userStorage.delete(user);
    }

    public List<User> findUsers() {
        return userStorage.findUsers();
    }

    public Optional<User> findUserById(long userId) {
        return userStorage.findUserById(userId);
    }

    public boolean addInFriends(long id, long friendId) {
        User friendRequest = userStorage.findUserById(id).get();
        User friendResponse = userStorage.findUserById(friendId).get();
        friendStorage.addInFriends(friendRequest, friendResponse);
        return true;
    }

    public boolean deleteFromFriends(long id, long friendId) {
        User friendRequest = userStorage.findUserById(id).get();
        User friendResponse = userStorage.findUserById(friendId).get();
        friendStorage.deleteFromFriends(friendRequest, friendResponse);
        return true;
    }

    public List<User> findFriends(long id) {
        return friendStorage.findFriends(id).stream()
                .map(userId -> findUserById(userId).get())
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(long id, long otherId) {
        return findFriends(id).stream()
                .filter(f -> findFriends(otherId).contains(f))
                .collect(Collectors.toList());
    }

}