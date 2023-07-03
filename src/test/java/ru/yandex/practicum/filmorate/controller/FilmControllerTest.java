package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.MarkStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.memoryImpl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.memoryImpl.InMemoryMarkStorage;
import ru.yandex.practicum.filmorate.storage.memoryImpl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void beforeEach() {
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        MarkStorage markStorage = new InMemoryMarkStorage();
        FilmService service = new FilmService(filmStorage, userStorage, markStorage, null);
        controller = new FilmController(service);
        InMemoryFilmStorage.filmsId = 0;
        film1 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2000, 01, 01));
        film1.setDuration(180);
        film2 = new Film("film 2", "FIlm 2 description",
                LocalDate.of(2010, 02, 22));
        film2.setDuration(122);
        film3 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2020, 03, 31));
        film3.setDuration(209);
    }

    @Test
    @DisplayName("тест создания фильма")
    void create() {
        controller.create(film1);
        final List<Film> films = new ArrayList<>(controller.findFilms());

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertTrue(films.contains(film1), "Фильм не совпадает.");
        assertEquals(film1, films.get(0), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильным названием")
    void createFailName() {
        film1.setName(" ");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film1));
        assertEquals("Ошибка! Название не может быть пустым. Код ошибки: 30001", exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильным описанием")
    void createFailDescription() {
        film1.setDescription("fail".repeat(51));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film1));
        assertEquals("Ошибка! Максимальная длина описания — 200 символов. Код ошибки: 30002",
                exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильной датой релиза")
    void createFailBirthday() {
        film1.setReleaseDate(LocalDate.of(1890, 01, 01));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film1));
        assertEquals("Ошибка! Дата релиза — не раньше 28 декабря 1895 года. Код ошибки: 30003",
                exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильной продолжительностью")
    void createWithEmptyName() {
        film1.setDuration(-100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film1));
        assertEquals("Ошибка! Продолжительность фильма должна быть положительной. Код ошибки: 30004", exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест обновления фильма")
    void update() {
        controller.create(film1);
        film2.setId(1);
        controller.update(film2);
        final List<Film> films = new ArrayList<>(controller.findFilms());

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertFalse(films.contains(film1), "Фильм совпадает.");
        assertTrue(films.contains(film2), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест обновления неизвестного фильма")
    void updateFail() {
        controller.create(film1);
        final FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> controller.update(film2));
        assertEquals(String.format("Фильм № %d не найден", film2.getId()), exception.getMessage());

        final List<Film> films = new ArrayList<>(controller.findFilms());

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertTrue(films.contains(film1), "Фильм не совпадает.");
        assertFalse(films.contains(film2), "Фильм совпадает.");
    }

    @Test
    @DisplayName("тест получения списка всех фильмов")
    void findUsers() {
        controller.create(film1);
        controller.create(film2);
        controller.create(film3);
        final List<Film> films = new ArrayList<>(controller.findFilms());

        assertNotNull(films, "Фильмы не возвращаются.");
        assertEquals(3, films.size(), "Неверное количество фильмов.");
        assertTrue(films.contains(film1), "Фильм не записался.");
        assertTrue(films.contains(film2), "Фильм не записался.");
        assertTrue(films.contains(film3), "Фильм не записался.");
    }

}