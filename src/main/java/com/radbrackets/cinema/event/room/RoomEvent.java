package com.radbrackets.cinema.event.room;

import com.radbrackets.cinema.event.Event;
import com.radbrackets.cinema.room.Room;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoomEvent(
    UUID id,
    Room room,
    LocalDateTime start,
    LocalDateTime end,
    RoomEventType eventType) implements Event {
}
