package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    boolean delete(User user);

    List<User> findUsers();

    void addInFriends(User friendRequest, User friendResponse);

    void deleteFromFriends(User friendRequest, User friendResponse);

    List<User> findMutualFriends(User friendRequest, User friendResponse);

}
