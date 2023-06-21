package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
        userStorage.isFindUserById(review.getUserId());
        filmStorage.isFindFilmById(review.getFilmId());
        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        reviewStorage.findReviewById(review.getReviewId());
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        reviewStorage.findReviewById(reviewId);
        reviewStorage.delete(reviewId);
    }

    public Review findReviewById(Long reviewId) {
        return reviewStorage.findReviewById(reviewId);
    }

    public List<Review> findReviews(Long filmId, Integer count) {
        return reviewStorage.findReviews(filmId, count);
    }

    public void addLike(Long reviewId, Long userId) {
        reviewStorage.findReviewById(reviewId);
        userStorage.isFindUserById(userId);
        reviewStorage.increaseUseful(reviewId);
    }

    public void addDislike(Long reviewId, Long userId) {
        reviewStorage.findReviewById(reviewId);
        userStorage.isFindUserById(userId);
        reviewStorage.decreaseUseful(reviewId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        reviewStorage.findReviewById(reviewId);
        userStorage.isFindUserById(userId);
        reviewStorage.decreaseUseful(reviewId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        reviewStorage.findReviewById(reviewId);
        userStorage.isFindUserById(userId);
        reviewStorage.increaseUseful(reviewId);
    }
}
