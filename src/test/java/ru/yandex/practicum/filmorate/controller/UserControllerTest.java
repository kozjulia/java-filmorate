package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.MarkStorage;
import ru.yandex.practicum.filmorate.storage.memoryImpl.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        InMemoryUserStorage storage = new InMemoryUserStorage();
        FriendStorage friendStorage = new InMemoryFriendStorage(storage);
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        MarkStorage gradeStorage = new InMemoryMarkStorage();
        UserService service = new UserService(storage, friendStorage, filmStorage, gradeStorage, null);
        controller = new UserController(service);
        InMemoryUserStorage.usersId = 0;
        user1 = new User("email1@mail.ru", "user1", LocalDate.of(1980, 01, 01));
        user1.setName("User 1 name");
        user2 = new User("email2@mail.ru", "user2", LocalDate.of(1981, 02, 02));
        user2.setName("User 2 name");
        user3 = new User("email3@mail.ru", "user3", LocalDate.of(1982, 03, 03));
        user3.setName("User 3 name");
    }

    @Test
    @DisplayName("тест создания пользователя")
    void create() {
        controller.create(user1);
        final List<User> users = new ArrayList<>(controller.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.contains(user1), "Пользователь не совпадает.");
        assertEquals(user1, users.get(0), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест создания пользователя с неправильной электронной почтой")
    void createFailEmail() {
        user1.setEmail("mail.ru");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user1));
        assertEquals("Ошибка! Неверный e-mail. Код ошибки: 30005", exception.getMessage());
        assertEquals(0, controller.findUsers().size(), "Пользователь найден.");
    }

    @Test
    @DisplayName("тест создания пользователя с неправильным логином")
    void createFailLogin() {
        user1.setLogin("user 1");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user1));
        assertEquals("Ошибка! Логин не может быть пустым и содержать пробелы. Код ошибки: 30006",
                exception.getMessage());
        assertEquals(0, controller.findUsers().size(), "Пользователь найден.");
    }

    @Test
    @DisplayName("тест создания пользователя с пустым именем")
    void createWithEmptyName() {
        user1.setName(null);
        controller.create(user1);
        final List<User> users = new ArrayList<>(controller.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.contains(user1), "Пользователь не совпадает.");
        assertEquals(user1.getLogin(), users.get(0).getName(), "Имя пользователя не совпадает.");
    }

    @Test
    @DisplayName("тест создания пользователя с неправильным днем рождения")
    void createFailBirthday() {
        user1.setBirthday(LocalDate.of(2034, 01, 01));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user1));
        assertEquals("Ошибка! Дата рождения не может быть в будущем. Код ошибки: 30007",
                exception.getMessage());
        assertEquals(0, controller.findUsers().size(), "Пользователь найден.");
    }

    @Test
    @DisplayName("тест обновления пользователя")
    void update() {
        controller.create(user1);
        user2.setId(1);
        controller.update(user2);
        final List<User> users = new ArrayList<>(controller.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertFalse(users.contains(user1), "Пользователь совпадает.");
        assertTrue(users.contains(user2), "Пользователь не совпадает.");
    }

    @Test
    @DisplayName("тест обновления неизвестного пользователя")
    void updateFail() {
        controller.create(user1);
        final UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> controller.update(user2));
        assertEquals(String.format("Пользователь № %d не найден", user2.getId()),
                exception.getMessage());

        final List<User> users = new ArrayList<>(controller.findUsers());

        assertNotNull(users, "Пользователь не найден.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertTrue(users.contains(user1), "Пользователь не совпадает.");
        assertFalse(users.contains(user2), "Пользователь совпадает.");
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