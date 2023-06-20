package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        return null;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return null;
    }

    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable Long reviewId) {

    }

    @GetMapping("/{reviewId}")
    public Review findReviewById(@PathVariable Long reviewId) {
        return null;
    }

    @GetMapping
    public List<Review> findReviews(@RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        return null;
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable Long reviewId, @PathVariable Long userId) {

    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable Long reviewId, @PathVariable Long userId) {

    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLike(@PathVariable Long reviewId, @PathVariable Long userId) {

    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long reviewId, @PathVariable Long userId) {

    }
}
