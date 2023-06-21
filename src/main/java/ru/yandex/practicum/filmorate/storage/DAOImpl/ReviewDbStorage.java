package ru.yandex.practicum.filmorate.storage.DAOImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public Review create(Review review) {
        String sqlQuery = "insert into reviews(review_content, is_positive, user_id, film_id, useful) " +
                " values (?, ?, ?, ?, ?);";
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
        return review;
    }

    @Override
    public Review update(Review review) {
        String sqlQuery = "update reviews set review_content = ?, is_positive = ? where review_id = ?;";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());

        return findReviewById(review.getReviewId());
    }

    @Override
    public void delete(Long reviewId) {
        String sqlQuery = "delete from reviews where review_id = ?;";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    @Override
    public Review findReviewById(Long reviewId) {
        String sqlQuery = "select * from reviews where review_id = ?;";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, reviewId);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Отзыв № {} не найден", reviewId);
            throw new ReviewNotFoundException(String.format("Отзыв № %d не найден", reviewId));
        }
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
    public void increaseUseful(Long reviewId) {
        String sqlQuery = "update reviews set useful = useful + 1 where review_id = ?;";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    @Override
    public void decreaseUseful(Long reviewId) {
        String sqlQuery = "update reviews set useful = useful - 1 where review_id = ?;";
        jdbcTemplate.update(sqlQuery, reviewId);
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
