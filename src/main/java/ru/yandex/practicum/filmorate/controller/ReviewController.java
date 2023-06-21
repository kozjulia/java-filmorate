package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        Review newReview = reviewService.create(review);
        log.debug("Добавлен новый отзыв: {}", newReview);
        return newReview;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        Review newReview = reviewService.update(review);
        log.debug("Обновлен отзыв: {}", newReview);
        return newReview;
    }

    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        log.debug("Удалён отзыв с ID: {}", reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review findReviewById(@PathVariable Long reviewId) {
        Review review = reviewService.findReviewById(reviewId);
        log.debug("Получен фильм с id = {}", reviewId);
        return review;
    }

    @GetMapping
    public List<Review> findReviews(@RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count < 0) {
            String message = "Параметр count не может быть отрицательным!";
            log.warn(message);
            throw new ValidationException(message);
        }
        List<Review> reviews = reviewService.findReviews(filmId, count);
        log.debug("Получен список отзывов, " +
                "количество = {}", reviews.size());
        return reviews;
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addLike(reviewId, userId);
        log.debug("Пользователь id = {} лайкнул отзыв id = {}", userId, reviewId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addDislike(reviewId, userId);
        log.debug("Пользователь id = {} дизлайкнул отзыв id = {}", userId, reviewId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.deleteLike(reviewId, userId);
        log.debug("Пользователь id = {} удалил лайк на отзыв id = {}", userId, reviewId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.deleteDislike(reviewId, userId);
        log.debug("Пользователь id = {} удалил дизлайк на отзыв id = {}", userId, reviewId);
    }
}
