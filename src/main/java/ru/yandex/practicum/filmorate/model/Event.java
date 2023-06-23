package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class Event {

    @NonNull
    private long eventId;
    private long userId;
    private long timestamp;
    private String eventType; // одно из значений LIKE, REVIEW или FRIEND
    private String operation; // одно из значениий REMOVE, ADD, UPDATE
    @NonNull
    private long entityId;  // идентификатор сущности, с которой произошло событие

}