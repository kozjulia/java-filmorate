package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    /**
     * добавление нового отзыва
     */
    public Review create(@Valid @RequestBody Review review) {
        Review newReview = reviewService.create(review);
        log.debug("Добавлен новый отзыв: {}", newReview);
        return newReview;
    }

    @PutMapping
    /**
     * обновление отзыва
     */
    public Review update(@Valid @RequestBody Review review) {
        Review newReview = reviewService.update(review);
        log.debug("Обновлен отзыв: {}", newReview);
        return newReview;
    }

    @DeleteMapping("/{reviewId}")
    /**
     * удаление отзыва
     */
    public void delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        log.debug("Удалён отзыв с ID: {}", reviewId);
    }

    @GetMapping("/{reviewId}")
    /**
     * получение отзыва по id
     */
    public Review findReviewById(@PathVariable Long reviewId) {
        Review review = reviewService.findReviewById(reviewId);
        log.debug("Получен отзыв с id = {}", reviewId);
        return review;
    }

    @GetMapping
    /**
     * получение списка отзывов
     */
    public List<Review> findReviews(@RequestParam(required = false) Long filmId,
                                    @RequestParam(defaultValue = "10", required = false) Integer count) {
        List<Review> reviews = reviewService.findReviews(filmId, count);
        log.debug("Получен список отзывов, " +
                "количество = {}", reviews.size());
        return reviews;
    }

    @PutMapping("/{reviewId}/like/{userId}")
    /**
     * пользователь лайкнул отзыв
     */
    public boolean addLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        if (reviewService.increaseUseful(reviewId, userId)) {
            log.debug("Пользователь id = {} лайкнул отзыв id = {}", userId, reviewId);
            return true;
        }
        return false;
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    /**
     * пользователь дизлайкнул отзыв
     */
    public boolean addDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
        if (reviewService.decreaseUseful(reviewId, userId)) {
            log.debug("Пользователь id = {} дизлайкнул отзыв id = {}", userId, reviewId);
            return true;
        }
        return false;
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    /**
     * пользователь удалил лайк на отзыв
     */
    public boolean deleteLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        if (reviewService.decreaseUseful(reviewId, userId)) {
            log.debug("Пользователь id = {} удалил лайк на отзыв id = {}", userId, reviewId);
            return true;
        }
        return false;
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    /**
     * пользователь удалил дизлайк на отзыв
     */
    public boolean deleteDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
        if (reviewService.increaseUseful(reviewId, userId)) {
            log.debug("Пользователь id = {} удалил дизлайк на отзыв id = {}", userId, reviewId);
            return true;
        }
        return false;
    }

}