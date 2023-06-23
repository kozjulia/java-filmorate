package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void beforeEach() {
        RatingMPA ratingMPA3 = new RatingMPA(3L, "PG-13", "детям до 13 лет просмотр не желателен");
        RatingMPA ratingMPA4 = new RatingMPA(4L, "R",
                "лицам до 17 лет просматривать фильм можно только в присутствии взрослого");
        Genre genre1 = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        Genre genre5 = new Genre(5L, "Документальный");
        film1 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2000, 01, 01));
        film1.setDuration(180);
        film1.setGenres(Set.of(genre1, genre2, genre5));
        film1.setMpa(ratingMPA4);
        film2 = new Film("film 2", "FIlm 2 description",
                LocalDate.of(2010, 02, 22));
        film2.setDuration(122);
        film2.setGenres(Set.of(genre2));
        film2.setMpa(ratingMPA3);
        film3 = new Film("film 1", "FIlm 1 description",
                LocalDate.of(2020, 03, 31));
        film3.setDuration(209);
        film3.setGenres(Set.of(genre1, genre5));
        film3.setMpa(ratingMPA4);
    }

    @Test
    @DisplayName("тест создания фильма")
    public void testCreateFilm() {
        film1 = filmStorage.create(film1).get();
        final List<Film> films = new ArrayList<>(filmStorage.findFilms());

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertTrue(films.contains(film1), "Фильм не совпадает.");
        assertEquals(film1, films.get(0), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест обновления фильма")
    void testUpdateFilm() {
        film1 = filmStorage.create(film1).get();
        film2.setId(1);
        film2 = filmStorage.update(film2).get();
        final List<Film> films = new ArrayList<>(filmStorage.findFilms());

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertFalse(films.contains(film1), "Фильм совпадает.");
        assertTrue(films.contains(film2), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест удаления фильма")
    void testDeleteFilm() {
        film1 = filmStorage.create(film1).get();
        film2 = filmStorage.create(film2).get();
        filmStorage.delete(film1);
        final List<Film> films = new ArrayList<>(filmStorage.findFilms());

        assertNotNull(films, "Фильм не найден.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertFalse(films.contains(film1), "Фильм совпадает.");
        assertTrue(films.contains(film2), "Фильм не совпадает.");
    }

    @Test
    @DisplayName("тест получения списка всех фильмов")
    void testFindUsers() {
        film1 = filmStorage.create(film1).get();
        film2 = filmStorage.create(film2).get();
        film3 = filmStorage.create(film3).get();
        final List<Film> films = new ArrayList<>(filmStorage.findFilms());

        assertNotNull(films, "Фильмы не возвращаются.");
        assertEquals(3, films.size(), "Неверное количество фильмов.");
        assertTrue(films.contains(film1), "Фильм не записался.");
        assertTrue(films.contains(film2), "Фильм не записался.");
        assertTrue(films.contains(film3), "Фильм не записался.");
    }

    @Test
    @DisplayName("тест нахождения фильма по ид")
    public void testFindFilmById() {
        filmStorage.create(film1);
        Optional<Film> filmOptional = filmStorage.findFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

}