package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FriendStorageTest {

    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        user1 = new User("email1@mail.ru", "user1", LocalDate.of(1980, 01, 01));
        user1.setName("User 1 name");
        user2 = new User("email2@mail.ru", "user2", LocalDate.of(1981, 02, 02));
        user2.setName("User 2 name");
        user3 = new User("email3@mail.ru", "user3", LocalDate.of(1982, 03, 03));
        user3.setName("User 3 name");
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
    }

    @Test
    @DisplayName("тест отправки заявки на добавление в друзья другому пользователю ")
    void addInFriends() {
        friendStorage.addInFriends(user1, user2);
        friendStorage.addInFriends(user1, user3);
        friendStorage.addInFriends(user2, user1);
        final List<Long> friends = new ArrayList<>(friendStorage.findFriends(user1.getId()));

        assertNotNull(friends, "Друзья не найдены.");
        assertEquals(2, friends.size(), "Неверное количество друзей.");
        assertTrue(friends.contains(user2.getId()), "Друзья не совпадают.");
        assertTrue(friends.contains(user3.getId()), "Друзья не совпадают.");
    }

    @Test
    @DisplayName("тест удаления из друзей другого пользователя ")
    void deleteFromFriends() {
        friendStorage.addInFriends(user1, user2);
        friendStorage.addInFriends(user2, user1);
        friendStorage.addInFriends(user2, user3);
        friendStorage.addInFriends(user3, user2);
        friendStorage.deleteFromFriends(user1, user2);
        final List<Long> friends = new ArrayList<>(friendStorage.findFriends(user2.getId()));

        assertNotNull(friends, "Друзья не найдены.");
        assertEquals(2, friends.size(), "Неверное количество друзей.");
        assertTrue(friends.contains(user1.getId()), "Друзья не совпадают.");
        assertTrue(friends.contains(user3.getId()), "Друзья не совпадают.");
    }

    @Test
    @DisplayName("тест получения списка пользователей, являющихся его друзьями ")
    void findFriends() {
        friendStorage.addInFriends(user1, user2);
        friendStorage.addInFriends(user2, user1);
        friendStorage.addInFriends(user1, user3);
        final List<Long> friends = new ArrayList<>(friendStorage.findFriends(user1.getId()));

        assertNotNull(friends, "Друзья не найдены.");
        assertEquals(2, friends.size(), "Неверное количество друзей.");
        assertTrue(friends.contains(user2.getId()), "Друзья не совпадают.");
        assertTrue(friends.contains(user3.getId()), "Друзья не совпадают.");
    }

}