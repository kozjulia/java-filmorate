package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidatorControllers;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{

    protected final Map<Long, User> users = new HashMap<>();
    private static long usersId = 1;  // сквозной счетчик

    @Override
    public User create(User user) {
        user = validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            ValidatorControllers.logAndError("Ошибка! Невозможно обновить пользователя - его не существует.");
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
    public void addInFriends(User friendRequest, User friendResponse) {
        Set<Long> friends = new HashSet<>();
        friends.addAll(users.get(friendRequest.getId()).getFriends());
        friends.add(friendResponse.getId());
        friendRequest.setFriends(friends);
        users.put(friendRequest.getId(), friendRequest);

        friends.clear();
        friends.addAll(users.get(friendResponse.getId()).getFriends());
        friends.add(friendRequest.getId());
        friendResponse.setFriends(friends);
        users.put(friendResponse.getId(), friendResponse);
    }

    @Override
    public void deleteFromFriends(User friendRequest, User friendResponse) {
        Set<Long> friends = new HashSet<>();
        friends.addAll(users.get(friendRequest.getId()).getFriends());
        friends.remove(friendResponse.getId());
        friendRequest.setFriends(friends);
        users.put(friendRequest.getId(), friendRequest);

        friends.clear();
        friends.addAll(users.get(friendResponse.getId()).getFriends());
        friends.remove(friendRequest.getId());
        friendResponse.setFriends(friends);
        users.put(friendResponse.getId(), friendResponse);
    }

    @Override
    public List<User> findMutualFriends(User friendRequest, User friendResponse) {
        Set<Long> setFriendsRequest = new HashSet<>();
        setFriendsRequest.addAll(friendRequest.getFriends());

        Set<Long> setFriendsResponse = new HashSet<>();
        setFriendsResponse.addAll(friendResponse.getFriends());

        return setFriendsRequest.stream()
                .filter(i -> setFriendsResponse.contains(i))
                .map(i -> users.get(i))
                .collect(Collectors.toList());
    }

    private static Long getNextId() {
        return usersId++;
    }

    private User validate(User user) {
        ValidatorControllers.validateEmail(user.getEmail());
        ValidatorControllers.validateLogin(user.getLogin());
        user = validateName(user);
        ValidatorControllers.validateBirthday(user.getBirthday());
        //user = validateId(user);
        return user;
    }

    /*private User validateId(User user) {
        if (user.getId() == 0) {
            user.setId(User.usersId++);
        }
        return user;
    }*/

    private User validateName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

}
