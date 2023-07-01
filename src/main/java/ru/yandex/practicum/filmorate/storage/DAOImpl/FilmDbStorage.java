package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Film> create(Film film) {
        String sqlQuery = "insert into films(film_name, description, release_date, duration, rate, rating_mpa_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());

            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        film.setMpa(findRatingMPAById(film.getMpa().getId()).get());
        Film updateFilm = updateGenres(film);
        updateFilm = updateDirectors(updateFilm);
        return Optional.of(updateFilm);
    }

    public Optional<Film> update(Film film) {
        String sqlQuery = "update films set film_name = ?, description = ?, release_date = ?, " +
                "duration = ?, rate = ?, rating_mpa_id = ? where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());

        film.setMpa(findRatingMPAById(film.getMpa().getId()).get());
        deleteGenres(film.getId());
        film = updateGenres(film);
        deleteFilmDirector(film.getId());
        film = updateDirectors(film);
        return Optional.of(film);
    }

    public boolean delete(Film film) {
        String sqlQuery = "delete from films where film_id = ?";
        return jdbcTemplate.update(sqlQuery, film.getId()) > 0;
    }

    public boolean deleteFilmById(long filmId) {
        String sqlQuery = "delete from films where film_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    public List<Film> findFilms() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    public Optional<Film> findFilmById(long filmId) {
        String sqlQuery = "select * from films where film_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, filmId));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Фильм № {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
    }

    public boolean isFindFilmById(long filmId) {
        String sqlQuery = "select exists(select 1 from films where film_id = ?)";
        if (jdbcTemplate.queryForObject(sqlQuery, Boolean.class, filmId)) {
            return true;
        }
        log.warn("Фильм № {} не найден", filmId);
        throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
    }

    public List<Genre> findGenres() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    public Optional<Genre> findGenreById(long genreId) {
        String sqlQuery = "select * from genres where genre_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, genreId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<RatingMPA> findRatingMPAs() {
        String sqlQuery = "select * from rating_mpa";
        return jdbcTemplate.query(sqlQuery, this::makeRatingMPA);
    }

    public Optional<RatingMPA> findRatingMPAById(long ratingMPAId) {
        String sqlQuery = "select * from rating_mpa where rating_mpa_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::makeRatingMPA, ratingMPAId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Director> findDirectors() {
        String sqlQuery = "select * from directors";
        return jdbcTemplate.query(sqlQuery, this::makeDirector);
    }

    public Optional<Director> findDirectorById(long directorId) {
        String sqlQuery = "select * from directors where director_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::makeDirector, directorId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean isFindDirectorById(long directorId) {
        String sqlQuery = "select exists(select 1 from directors where director_id = ?)";
        if (jdbcTemplate.queryForObject(sqlQuery, Boolean.class, directorId)) {
            return true;
        }
        log.warn("Режиссёр № {} не найден", directorId);
        throw new DirectorNotFoundException(String.format("Режиссёр № %d не найден", directorId));
    }

    public Optional<Director> createDirector(Director director) {
        String sqlQuery = "insert into directors(director_name) " +
                " values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        director.setId(keyHolder.getKey().longValue());
        return Optional.of(director);
    }

    public Optional<Director> updateDirector(Director director) {
        String sqlQuery = "update directors set director_name = ? where director_id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return Optional.of(director);
    }

    public boolean deleteDirectorById(Long directorId) {
        String sqlQuery = "delete from directors where director_id = ?";
        return jdbcTemplate.update(sqlQuery, directorId) > 0;
    }

    public List<Film> findSortFilmsByDirector(long directorId, String sortBy) {
        if (sortBy.equals("likes")) {
            String sqlQuery = "select f.*, AVG(g.mark) as avg_marks " +
                    "from films as f " +
                    "INNER JOIN film_director as fd ON fd.film_id = f.film_id " +
                    "LEFT OUTER JOIN marks as g ON g.film_id = f.film_id " +
                    "where fd.director_id = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER by avg_marks DESC";
            return jdbcTemplate.query(sqlQuery, this::makeFilm, directorId);
        }
        if (sortBy.equals("year")) {
            String sqlQuery = "select f.*,  EXTRACT(YEAR FROM CAST(f.release_date AS date)) as year_film " +
                    "from films as f " +
                    "INNER JOIN film_director as fd ON fd.film_id = f.film_id " +
                    "where fd.director_id = ? " +
                    "ORDER by year_film ";
            return jdbcTemplate.query(sqlQuery, this::makeFilm, directorId);
        }
        return Collections.EMPTY_LIST;
    }

    public List<Film> findSortFilmsBySubstring(String query, boolean isDirector, boolean isTitle) {
        String where = "";
        String joinDirector = "LEFT OUTER JOIN film_director as fd ON fd.film_id = f.film_id " +
                "LEFT OUTER JOIN DIRECTORS as d ON d.director_id = fd.DIRECTOR_ID ";
        if (isDirector && isTitle) {
            where = "WHERE LOWER(f.FILM_NAME) LIKE LOWER('%" + query + "%') OR " +
                    "LOWER(d.DIRECTOR_NAME) LIKE LOWER('%" + query + "%') ";
        } else if (isDirector) {
            where = "WHERE LOWER(d.DIRECTOR_NAME) LIKE LOWER('%" + query + "%') ";
        } else if (isTitle) {
            joinDirector = "";
            where = "WHERE LOWER(f.FILM_NAME) LIKE LOWER('%" + query + "%') ";
        }

        String sqlQuery = "SELECT  f.*, AVG(g.mark) as avg_marks " +
                "FROM films as f " +
                joinDirector +
                "LEFT OUTER JOIN marks as g ON g.film_id = f.film_id " +
                where +
                "GROUP BY f.film_id " +
                "ORDER BY avg_marks DESC, f.film_id DESC";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    public List<Film> findCommonSortFilms(long userId, long friendId) {
        String sqlQuery = "SELECT f.*, AVG(g.mark) as avg_marks " +
                "FROM films as f " +
                "LEFT OUTER JOIN marks as g ON g.film_id = f.film_id " +
                "WHERE f.film_id IN (SELECT film_id FROM MARKS WHERE user_id = ?) " +
                "AND f.film_id IN (SELECT film_id FROM MARKS WHERE user_id = ?) " +
                "GROUP BY f.film_id " +
                "ORDER BY avg_marks DESC ";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, userId, friendId);
    }

    private void createGenre(Long filmId, Genre genre) {
        String sqlQuery = "insert into film_genre(film_id, genre_id) " +
                " values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genre.getId());
    }

    private void createFilmDirector(Long filmId, Director director) {
        String sqlQuery = "insert into film_director(film_id, director_id) " +
                " values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, director.getId());
    }

    private Film updateGenres(Film film) {
        film.setGenres(film.getGenres().stream()
                .map(genre -> findGenreById(genre.getId()).get())
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toSet()));
        for (Genre genre : film.getGenres()) {
            createGenre(film.getId(), genre);
        }
        return film;
    }

    private Film updateDirectors(Film film) {
        film.setDirectors(film.getDirectors().stream()
                .map(director -> findDirectorById(director.getId()).get())
                .collect(Collectors.toSet()));
        for (Director director : film.getDirectors()) {
            createFilmDirector(film.getId(), director);
        }
        return film;
    }

    private void deleteGenres(Long filmId) {
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void deleteFilmDirector(Long filmId) {
        String sqlQuery = "delete from film_director where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private List<Genre> findGenresByFilmId(Long filmId) {
        String sqlQuery = "select * from film_genre as fg " +
                "join genres as g on g.genre_id = fg.genre_id " +
                "where fg.film_id = ? order by genre_id";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, filmId);
    }

    private List<Director> findDirectorsByFilmId(Long filmId) {
        String sqlQuery = "select * from film_director as fd " +
                "join directors as d on d.director_id = fd.director_id " +
                "where fd.film_id = ? order by director_id";
        return jdbcTemplate.query(sqlQuery, this::makeDirector, filmId);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(rs.getString("film_name"), rs.getString("description"),
                rs.getDate("release_date").toLocalDate());
        film.setId(rs.getLong("film_id"));
        film.setDuration(rs.getInt("duration"));
        film.setRate(rs.getInt("rate"));
        film.setGenres(new HashSet<>(findGenresByFilmId(film.getId())));
        film.setMpa(findRatingMPAById(rs.getLong("rating_mpa_id")).get());
        film.setDirectors(new HashSet<>(findDirectorsByFilmId(film.getId())));
        return film;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
    }

    private RatingMPA makeRatingMPA(ResultSet rs, int rowNum) throws SQLException {
        return new RatingMPA(rs.getLong("rating_mpa_id"),
                rs.getString("rating_mpa_name"), rs.getString("description"));
    }

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("director_id"), rs.getString("director_name"));
    }

}