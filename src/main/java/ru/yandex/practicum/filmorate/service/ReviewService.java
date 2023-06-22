package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventStorage;
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
    @Qualifier("eventDbStorage")
    private final EventStorage eventStorage;

    public Review create(Review review) {
        if (!filmStorage.isFindFilmById(review.getFilmId()) || !userStorage.isFindUserById(review.getUserId())) {
            return null;
        }
        reviewStorage.create(review);
        eventStorage.createEvent(review.getUserId(), "REVIEW", "ADD", review.getReviewId());
        return review;
    }

    public Review update(Review review) {
        if (!reviewStorage.isFindReviewById(review.getReviewId())) {
            return null;
        }
        reviewStorage.update(review);
        eventStorage.createEvent(review.getUserId(), "REVIEW", "UPDATE", review.getReviewId());
        return review;
    }

    public boolean delete(Long reviewId) {
        if (!reviewStorage.isFindReviewById(reviewId)) {
            return false;
        }
        eventStorage.createEvent(findReviewById(reviewId).getUserId(), "REVIEW", "REMOVE", findReviewById(reviewId).getReviewId());
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
