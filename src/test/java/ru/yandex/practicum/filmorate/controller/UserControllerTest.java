package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        controller = new UserController();
        User.usersId = 1;
        user1 = new User("email1@mail.ru", "user1",
                "User 1 description", LocalDate.of(1980, 01, 01));
        user2 = new User("email2@mail.ru", "user2",
                "User 2 description", LocalDate.of(1981, 02, 02));
        user3 = new User("email3@mail.ru", "user3",
                "User 3 description", LocalDate.of(1982, 03, 03));
    }

    @Test
    @DisplayName("тест создания пользователя")
    void create() {
        controller.create(user1);
        final Map<Integer, User> users = new HashMap<>(controller.users);

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.containsKey(1), "Пользователь не совпадает.");
        assertEquals(user1, users.get(1), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест создания пользователя с неправильной электронной почтой")
    void createFailEmail() {
        User userFail = new User("mail.ru", "user 1",
                "", LocalDate.of(1980, 01, 01));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(userFail));
        assertEquals("Ошибка! Неверный e-mail.", exception.getMessage());
        assertEquals(0, controller.findUsers().size(), "Пользователь найден.");
    }

    @Test
    @DisplayName("тест создания пользователя с неправильным логином")
    void createFailLogin() {
        User userFail = new User("email1@mail.ru", "user 1",
                "User 1 description", LocalDate.of(1980, 01, 01));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(userFail));
        assertEquals("Ошибка! Логин не может быть пустым и содержать пробелы.",
                exception.getMessage());
        assertEquals(0, controller.findUsers().size(), "Пользователь найден.");
    }

    @Test
    @DisplayName("тест создания пользователя с пустым именем")
    void createWithEmptyName() {
        User user = new User("email1@mail.ru", "user1", LocalDate.of(1980, 01, 01));
        controller.create(user);
        final Map<Integer, User> users = new HashMap<>(controller.users);

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.containsKey(4), "Пользователь не совпадает.");
        assertEquals(user, users.get(4), "Пользователь не совпадает.");
        assertEquals(user.getLogin(), users.get(4).getName(), "Имя пользователя не совпадает.");
    }

    @Test
    @DisplayName("тест создания пользователя с неправильным днем рождения")
    void createFailBirthday() {
        User userFail = new User("email1@mail.ru", "user1",
                "User 1 description", LocalDate.of(2034, 01, 01));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(userFail));
        assertEquals("Ошибка! Дата рождения не может быть в будущем.",
                exception.getMessage());
        assertEquals(0, controller.findUsers().size(), "Пользователь найден.");
    }

    @Test
    @DisplayName("тест обновления пользователя")
    void update() {
        controller.create(user1);
        User userUpdate = new User(1, "emailUpdate@mail.ru", "user1Update",
                "User 1 update description", LocalDate.of(1985, 05, 05));
        controller.update(userUpdate);
        final Map<Integer, User> users = new HashMap<>(controller.users);

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.containsKey(1), "Пользователь не совпадает.");
        assertNotEquals(user1, users.get(1), "Пользователь совпадает.");
        assertEquals(userUpdate, users.get(1), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест обновления неизвестного пользователя")
    void updateFail() {
        controller.create(user1);
        User userFail = new User(999, "email1@mail.ru", "user1",
                "User 1 description", LocalDate.of(1980, 01, 01));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(userFail));
        assertEquals("Ошибка! Невозможно обновить пользователя - его не существует.",
                exception.getMessage());

        final Map<Integer, User> users = new HashMap<>(controller.users);

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.containsKey(1), "Пользователь не совпадает.");
        assertEquals(user1, users.get(1), "Пользователь совпадает.");
    }

    @Test
    @DisplayName("тест получения списка всех пользователей")
    void findUsers() {
        controller.create(user1);
        controller.create(user2);
        controller.create(user3);
        final List<User> users = new ArrayList<>(controller.findUsers());

        assertNotNull(users, "Пользователи не возвращаются.");
        assertEquals(3, users.size(), "Неверное количество пользователей.");
        assertTrue(users.contains(user1), "Пользователь не записался.");
        assertTrue(users.contains(user2), "Пользователь не записался.");
        assertTrue(users.contains(user3), "Пользователь не записался.");
    }
}