package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Event {

    @NonNull
    private Long eventId;
    @NonNull
    private Long userId;
    @NonNull
    private Long timestamp;
    private String eventType; // одно из значений LIKE, REVIEW или FRIEND
    private String operation; // одно из значениий REMOVE, ADD, UPDATE
    @NonNull
    private Long entityId;  // идентификатор сущности, с которой произошло событие

}