package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public Review create(Review review) {
        if (!filmStorage.isFindFilmById(review.getFilmId()) || !userStorage.isFindUserById(review.getUserId())) {
            return null;
        }
        return reviewStorage.create(review).get();
    }

    public Review update(Review review) {
        if (!reviewStorage.isFindReviewById(review.getReviewId())) {
            return null;
        }
        return reviewStorage.update(review).get();
    }

    public boolean delete(Long reviewId) {
        if (!reviewStorage.isFindReviewById(reviewId)) {
            return false;
        }
        return reviewStorage.delete(reviewId);
    }

    public Review findReviewById(Long reviewId) {
        return reviewStorage.findReviewById(reviewId).get();
    }

    public List<Review> findReviews(Long filmId, Integer count) {
        if (filmId != null && !filmStorage.isFindFilmById(filmId)) {
            return null;
        }
        return reviewStorage.findReviews(filmId, count);
    }

    public boolean increaseUseful(Long reviewId, Long userId) {
        if (!reviewStorage.isFindReviewById(reviewId) || !userStorage.isFindUserById(userId)) {
            return false;
        }
        reviewStorage.increaseUseful(reviewId);
        return true;
    }

    public boolean decreaseUseful(Long reviewId, Long userId) {
        if (!reviewStorage.isFindReviewById(reviewId) || !userStorage.isFindUserById(userId)) {
            return false;
        }
        reviewStorage.decreaseUseful(reviewId);
        return true;
    }
}
