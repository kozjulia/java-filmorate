package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Qualifier("reviewDbStorage")
    private final ReviewStorage reviewStorage;
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
        long reviewAuthorId = findReviewById(review.getReviewId()).getUserId();
        eventStorage.createEvent(reviewAuthorId, "REVIEW", "UPDATE", review.getReviewId());
        return reviewStorage.update(review).get();
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
        if (count < 0) {
            String message = "Параметр count не может быть отрицательным!";
            log.warn(message);
            throw new ValidationException(message, 20001);
        }
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