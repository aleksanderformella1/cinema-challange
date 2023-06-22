package com.radbrackets.cinema;

import lombok.RequiredArgsConstructor;

import com.radbrackets.cinema.movie.MovieService;
import com.radbrackets.cinema.room.RoomService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class CinemaInitializer {

  private final RoomService roomService;
  private final MovieService movieService;

  private static final List<String> movieCatalog = List.of(
      "Shrek",
      "Godfather",
      "Casablanca"
  );

  private static final Map<String, Duration> roomsCleaningTimes = Map.of(
      "Room 1", Duration.ofMinutes(15),
      "Room 2", Duration.ofMinutes(5),
      "Room 3", Duration.ofMinutes(45)
  );

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    movieCatalog.forEach(movieService::addNew);
    roomsCleaningTimes.forEach(roomService::addNew);
  }
}
