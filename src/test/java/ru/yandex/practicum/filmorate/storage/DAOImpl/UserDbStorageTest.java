package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserDbStorage userStorage;
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
    }

    @Test
    @DisplayName("тест создания пользователя")
    public void testCreateUser() {
        user1 = userStorage.create(user1).get();
        final List<User> users = new ArrayList<>(userStorage.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.contains(user1), "Пользователь не совпадает.");
        assertEquals(user1, users.get(0), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест обновления пользователя")
    void testUpdateUser() {
        user1 = userStorage.create(user1).get();
        user2.setId(1);
        user2 = userStorage.update(user2).get();
        final List<User> users = new ArrayList<>(userStorage.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertFalse(users.contains(user1), "Пользователь совпадает.");
        assertTrue(users.contains(user2), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест удаления пользователя")
    void testDeleteUser() {
        user1 = userStorage.create(user1).get();
        user2 = userStorage.create(user2).get();
        userStorage.delete(user1);
        final List<User> users = new ArrayList<>(userStorage.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertFalse(users.contains(user1), "Пользователь совпадает.");
        assertTrue(users.contains(user2), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест получения списка всех пользователей")
    void testFindUsers() {
        user1 = userStorage.create(user1).get();
        user2 = userStorage.create(user2).get();
        user3 = userStorage.create(user3).get();
        final List<User> users = new ArrayList<>(userStorage.findUsers());

        assertNotNull(users, "Пользователи не возвращаются.");
        assertEquals(3, users.size(), "Неверное количество пользователей.");
        assertTrue(users.contains(user1), "Пользователь не записался.");
        assertTrue(users.contains(user2), "Пользователь не записался.");
        assertTrue(users.contains(user3), "Пользователь не записался.");
    }

    @Test
    @DisplayName("тест нахождения пользователя по ид")
    public void testFindUserById() {
        userStorage.create(user1);
        Optional<User> userOptional = userStorage.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

}