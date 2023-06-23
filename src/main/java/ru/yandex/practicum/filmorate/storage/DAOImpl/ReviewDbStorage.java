package ru.yandex.practicum.filmorate.storage.DAOImpl;

import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

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
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> create(Review review) {
        String sqlQuery = "insert into reviews(review_content, is_positive, user_id, film_id, useful) " +
                "values (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());
            ps.setLong(3, review.getUserId());
            ps.setLong(4, review.getFilmId());
            ps.setInt(5, 0);
            return ps;
        }, keyHolder);
        review.setReviewId((Long) keyHolder.getKey());
        review.setUseful(0);
        return Optional.of(review);
    }

    @Override
    public Optional<Review> update(Review review) {
        String sqlQuery = "update reviews set review_content = ?, is_positive = ? where review_id = ?;";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());
        return findReviewById(review.getReviewId());
    }

    @Override
    public boolean delete(Long reviewId) {
        String sqlQuery = "delete from reviews where review_id = ?;";
        return jdbcTemplate.update(sqlQuery, reviewId) > 0;
    }

    @Override
    public Optional<Review> findReviewById(Long reviewId) {
        String sqlQuery = "select * from reviews where review_id = ?;";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, reviewId));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Отзыв № {} не найден", reviewId);
            throw new ReviewNotFoundException(String.format("Отзыв № %d не найден", reviewId));
        }
    }

    @Override
    public boolean isFindReviewById(Long reviewId) {
        String sqlQuery = "select exists(select 1 from reviews where review_id = ?)";
        if (jdbcTemplate.queryForObject(sqlQuery, Boolean.class, reviewId)) {
            return true;
        }
        log.warn("Отзыв № {} не найден", reviewId);
        throw new ReviewNotFoundException(String.format("Отзыв № %d не найден", reviewId));
    }

    @Override
    public List<Review> findReviews(Long filmId, Integer count) {
        String sqlQuery = "select * from reviews ";
        if (filmId == null) {
            sqlQuery += "order by useful desc limit ?;";
            return jdbcTemplate.query(sqlQuery, this::mapRowToReview, count);
        }
        sqlQuery += "where film_id = ? order by useful desc limit ?;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, filmId, count);
    }

    @Override
    public boolean increaseUseful(Long reviewId) {
        String sqlQuery = "update reviews set useful = useful + 1 where review_id = ?;";
        jdbcTemplate.update(sqlQuery, reviewId);
        return true;
    }

    @Override
    public boolean decreaseUseful(Long reviewId) {
        String sqlQuery = "update reviews set useful = useful - 1 where review_id = ?;";
        jdbcTemplate.update(sqlQuery, reviewId);
        return true;
    }

    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getLong("review_id"))
                .content(rs.getString("review_content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getLong("user_id"))
                .filmId(rs.getLong("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

}