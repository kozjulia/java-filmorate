package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Qualifier("friendDbStorage")
    private final FriendStorage friendStorage;
    @Qualifier("eventDbStorage")
    private final EventStorage eventStorage;

    public User create(User user) {
        return userStorage.create(user).get();
    }

    public User update(User user) {
        if (!userStorage.isFindUserById(user.getId())) {
            return null;
        }
        return userStorage.update(user).get();
    }

    public boolean deleteUserById(long userId) {
        if (!userStorage.isFindUserById(userId)) {
            return false;
        }
        return userStorage.deleteUserById(userId);
    }

    public boolean delete(User user) {
        if (!userStorage.isFindUserById(user.getId())) {
            return false;
        }
        return userStorage.delete(user);
    }

    public List<User> findUsers() {
        return userStorage.findUsers();
    }

    public User findUserById(long userId) {
        return userStorage.findUserById(userId).get();
    }

    public boolean addInFriends(long id, long friendId) {
        if (!userStorage.isFindUserById(id) || !userStorage.isFindUserById(friendId)) {
            return false;
        }
        User friendRequest = userStorage.findUserById(id).get();
        User friendResponse = userStorage.findUserById(friendId).get();
        friendStorage.addInFriends(friendRequest, friendResponse);
        eventStorage.createEvent(id, "FRIEND", "ADD", friendId);
        return true;
    }

    public boolean deleteFromFriends(long id, long friendId) {
        if (!userStorage.isFindUserById(id) || !userStorage.isFindUserById(friendId)) {
            return false;
        }
        User friendRequest = userStorage.findUserById(id).get();
        User friendResponse = userStorage.findUserById(friendId).get();
        friendStorage.deleteFromFriends(friendRequest, friendResponse);
        eventStorage.createEvent(id, "FRIEND", "REMOVE", friendId);
        return true;
    }

    public List<User> findFriends(long id) {
        if (!userStorage.isFindUserById(id)) {
            return Collections.EMPTY_LIST;
        }
        return friendStorage.findFriends(id).stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(long id, long otherId) {
        if (!userStorage.isFindUserById(id) || !userStorage.isFindUserById(otherId)) {
            return Collections.EMPTY_LIST;
        }
        return findFriends(id).stream()
                .filter(f -> findFriends(otherId).contains(f))
                .collect(Collectors.toList());
    }

    public List<Event> getUserFeed(Integer userId) {
        userStorage.findUserById(userId).get();
        return userStorage.getUserEvent(userId);
    }
}