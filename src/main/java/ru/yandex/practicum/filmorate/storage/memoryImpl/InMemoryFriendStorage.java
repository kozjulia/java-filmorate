package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InMemoryFriendStorage implements FriendStorage {

    private final InMemoryUserStorage userStorage;

    public boolean addInFriends(User friendRequest, User friendResponse) {
        userStorage.findUserById(friendRequest.getId()).get().getFriends().add(friendResponse.getId());
        userStorage.findUserById(friendResponse.getId()).get().getFriends().add(friendRequest.getId());
        return true;
    }

    public boolean deleteFromFriends(User friendRequest, User friendResponse) {
        userStorage.findUserById(friendRequest.getId()).get().getFriends().remove(friendResponse.getId());
        userStorage.findUserById(friendResponse.getId()).get().getFriends().remove(friendRequest.getId());
        return true;
    }

    public List<Long> findFriends(long id) {
        return new ArrayList<>(userStorage.findUserById(id).get().getFriends());
    }

}