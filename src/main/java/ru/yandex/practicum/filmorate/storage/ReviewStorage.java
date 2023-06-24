package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Optional<Review> create(Review review);

    Optional<Review> update(Review review);

    boolean delete(Long reviewId);

    Optional<Review> findReviewById(Long reviewId);

    boolean isFindReviewById(Long reviewId);

    List<Review> findReviews(Long filmId, Integer count);

    boolean increaseUseful(Long reviewId);

    boolean decreaseUseful(Long reviewId);

}