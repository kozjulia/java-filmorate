package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Qualifier("friendDbStorage")
    private final FriendStorage friendStorage;
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("markDbStorage")
    private final MarkStorage markStorage;
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

    public List<Film> getRecommendations(Long id) {
        // проверка id пользователя
        if (!userStorage.isFindUserById(id)) {
            return null;
        }
        Map<Long, Set<Long>> usersWithPositiveMarks = markStorage.findAllUsersWithPositiveMarks();
        // Set с filmId для пользователя id
        Set<Long> userMarkFilms = usersWithPositiveMarks.get(id);
        usersWithPositiveMarks.remove(id);
        if (userMarkFilms == null || usersWithPositiveMarks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        // Ищем пользователя с наибольшим совпадением
        Long userIdWithTopFreq = -1L;
        int topFreq = -1;
        for (Long userId : usersWithPositiveMarks.keySet()) {
            Set<Long> filmsId = new HashSet<>(usersWithPositiveMarks.get(userId));
            filmsId.retainAll(userMarkFilms);
            int countFreq = filmsId.size();
            if (countFreq > topFreq) {
                topFreq = countFreq;
                userIdWithTopFreq = userId;
            }
        }
        // Получаем Set с filmId для пользователя с наибольшим совпадением
        Set<Long> filmsId = usersWithPositiveMarks.get(userIdWithTopFreq);
        // Удаляем совпадающие filmId
        filmsId.removeAll(userMarkFilms);
        // Получаем список фильмов
        List<Film> films = new ArrayList<>();
        for (Long filmId : filmsId) {
            films.add(filmStorage.findFilmById(filmId).stream()
                    .peek(f -> f.setLikes(new HashSet<>(markStorage.findLikes(f))))
                    .peek(film -> film.setMarks(new HashSet<>(markStorage.findMarks(film))))
                    .findFirst().get());
        }

        return films;
    }

    public List<Event> getUserEvent(Integer userId) {
        userStorage.findUserById(userId).get();
        return userStorage.getUserEvent(userId);
    }

}