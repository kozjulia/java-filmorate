package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review create(Review review);

    Review update(Review review);

    void delete(Long reviewId);

    Review findReviewById(Long reviewId);

    List<Review> findReviews(Long filmId, Integer count);

    void increaseUseful(Long reviewId);

    void decreaseUseful(Long reviewId);
}
