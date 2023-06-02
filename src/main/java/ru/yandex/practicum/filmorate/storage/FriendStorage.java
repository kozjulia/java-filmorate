package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    boolean addInFriends(User friendRequest, User friendResponse);

    boolean deleteFromFriends(User friendRequest, User friendResponse);

    List<Long> findFriends(long id);

}