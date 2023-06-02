package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LikeDbStorageTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;

    private Film film1;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private RatingMPA ratingMPA3;
    private Genre genre1;
    private Genre genre2;

    @BeforeEach
    public void beforeEach() {
        ratingMPA3 = new RatingMPA(3, "PG-13", "детям до 13 лет просмотр не желателен");
        genre1 = new Genre(1, "Комедия");
        genre2 = new Genre(2, "Драма");
        film1 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2000, 01, 01));
        film1.setDuration(180);
        film1.setLikes(Set.of(1L, 3L, 4L));
        film1.setGenres(Set.of(genre1, genre2));
        film1.setMpa(ratingMPA3);
        user1 = new User("email1@mail.ru", "user1", LocalDate.of(1980, 01, 01));
        user1.setName("User 1 name");
        user2 = new User("email2@mail.ru", "user2", LocalDate.of(1981, 02, 02));
        user2.setName("User 2 name");
        user3 = new User("email3@mail.ru", "user3", LocalDate.of(1982, 03, 03));
        user3.setName("User 3 name");
        user4 = new User("email5@mail.ru", "user5", LocalDate.of(1985, 05, 05));
        user4.setName("User 5 name");
        filmStorage.create(film1);
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);
    }

    @Test
    @DisplayName("тест добавления фильму id всех лайкнувших пользователей")
    void like() {
        likeStorage.like(film1);
        final List<Long> userIds = new ArrayList<>(likeStorage.findLikes(film1));

        assertNotNull(userIds, "Лайки не найдены.");
        assertEquals(3, userIds.size(), "Неверное количество лайков.");
        assertTrue(userIds.contains(1L), "Лайки не совпадает.");
        assertTrue(userIds.contains(3L), "Лайки не совпадает.");
        assertTrue(userIds.contains(4L), "Лайки не совпадает.");
    }

    @Test
    @DisplayName("тест удаления у фильма id всех лайкнувших пользователей")
    void dislike() {
        likeStorage.dislike(film1);
        final List<Long> userIds = new ArrayList<>(likeStorage.findLikes(film1));

        assertEquals(0, userIds.size(), "Лайки найдены.");
        assertEquals(0, userIds.size(), "Неверное количество лайков.");
    }

    @Test
    @DisplayName("тест получения у фильма id всех лайкнувших пользователей")
    void findLikes() {
        likeStorage.like(film1);
        film1.setLikes(Set.of(1L, 2L));
        likeStorage.dislike(film1);
        likeStorage.like(film1);
        final List<Long> userIds = new ArrayList<>(likeStorage.findLikes(film1));

        assertNotNull(userIds, "Лайки не найдены.");
        assertEquals(2, userIds.size(), "Неверное количество лайков.");
        assertTrue(userIds.contains(1L), "Лайки не совпадает.");
        assertTrue(userIds.contains(2L), "Лайки не совпадает.");
    }

}