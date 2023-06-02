package ru.yandex.practicum.filmorate.storage.memoryImpl;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class InMemoryFriendStorage implements FriendStorage {

    public boolean addInFriends(User friendRequest, User friendResponse) {
        return false;
    }

    public boolean deleteFromFriends(User friendRequest, User friendResponse) {
        return false;
    }

    public List<Long> findFriends(long id) {
        return Collections.EMPTY_LIST;
    }

}