package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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
        controller = new FilmController();
        Film.filmsId = 1;
        film1 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2000, 01, 01), 180);
        film2 = new Film("film 2", "FIlm 2 description",
                LocalDate.of(2010, 02, 22), 122);
        film3 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2020, 03, 31), 209);
    }

    @Test
    @DisplayName("тест создания фильма")
    void create() {
        controller.create(film1);
        final Map<Integer, Film> films = new HashMap<>(controller.films);

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertTrue(films.containsKey(1), "Фильм не совпадает.");
        assertEquals(film1, films.get(1), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильным названием")
    void createFailName() {
        Film filmFail = new Film(" ", "FIlm fail description",
                LocalDate.of(2000, 01, 01), 180);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(filmFail));
        assertEquals("Ошибка! Название не может быть пустым.", exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильным описанием")
    void createFailDescription() {
        Film filmFail = new Film("film fail", "Пятеро друзей ( комик-группа «Шарло»), приезжают " +
                "в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(2000, 01, 01), 180);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(filmFail));
        assertEquals("Ошибка! Максимальная длина описания — 200 символов.",
                exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильной датой релиза")
    void createFailBirthday() {
        Film filmFail = new Film("film fail", "FIlm fail description",
                LocalDate.of(1890, 01, 01), 180);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(filmFail));
        assertEquals("Ошибка! Дата релиза — не раньше 28 декабря 1895 года.",
                exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест создания фильма с неправильной продолжительностью")
    void createWithEmptyName() {
        Film filmFail = new Film("film fail", "FIlm fail description",
                LocalDate.of(2000, 01, 01), -100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(filmFail));
        assertEquals("Ошибка! Продолжительность фильма должна быть положительной.", exception.getMessage());
        assertEquals(0, controller.findFilms().size(), "Фильм найден.");
    }

    @Test
    @DisplayName("тест обновления фильма")
    void update() {
        controller.create(film1);
        Film filmUpdate = new Film(1, "film update", "FIlm update description",
                LocalDate.of(2001, 01, 01), 100, 4);
        controller.update(filmUpdate);
        final Map<Integer, Film> films = new HashMap<>(controller.films);

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertTrue(films.containsKey(1), "Фильм не совпадает.");
        assertNotEquals(film1, films.get(1), "Фильм совпадает.");
        assertEquals(filmUpdate, films.get(1), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест обновления неизвестного фильма")
    void updateFail() {
        controller.create(film1);
        Film filmUpdate = new Film(999, "film 1", "FIlm 1 description",
                LocalDate.of(2000, 01, 01), 180, 4);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(filmUpdate));
        assertEquals("Ошибка! Невозможно обновить фильм - его не существует.", exception.getMessage());

        final Map<Integer, Film> films = new HashMap<>(controller.films);

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertTrue(films.containsKey(1), "Фильм не совпадает.");
        assertEquals(film1, films.get(1), "Фильм совпадает.");
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
        assertTrue(films.contains(film1), "Фильм не записался.");
        assertTrue(films.contains(film1), "Фильм не записался.");
    }
}