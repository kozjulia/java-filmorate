package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Error {
    public ValidationException(String message) {
        super(message);
    }
}