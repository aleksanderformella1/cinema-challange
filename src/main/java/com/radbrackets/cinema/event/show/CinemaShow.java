package com.radbrackets.cinema.event.show;

import com.radbrackets.cinema.event.Event;
import com.radbrackets.cinema.movie.Movie;
import com.radbrackets.cinema.room.Room;

import java.time.LocalDateTime;
import java.util.UUID;

public record CinemaShow(

    UUID id,
    Movie movie,
    Room room,
    ShowType showType,
    LocalDateTime start,
    LocalDateTime end) implements Event {
}
