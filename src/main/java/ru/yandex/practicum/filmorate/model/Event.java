package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class Event {
    private long timestamp;
    private long userId;
    private String eventType;
    private String operation;
    @NonNull
    private long eventId;
    @NonNull
    private long entityId;
}
