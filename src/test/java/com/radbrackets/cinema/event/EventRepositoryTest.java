package com.radbrackets.cinema.event;

import com.google.common.collect.Range;
import com.radbrackets.cinema.event.show.CinemaShow;
import com.radbrackets.cinema.event.show.CinemaShowRepository;
import com.radbrackets.cinema.event.show.ShowType;
import com.radbrackets.cinema.movie.Movie;
import com.radbrackets.cinema.room.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventRepositoryTest {

  private final EventRepository<CinemaShow> repository = new CinemaShowRepository();

  private static final UUID showId = UUID.randomUUID();
  private static final Room room = new Room(randomUUID(), "Room 1", Duration.ofMinutes(5));
  private static final CinemaShow show = new CinemaShow(
      showId,
      new Movie(randomUUID(), "Shrek"),
      room,
      ShowType.REGULAR,
      LocalDateTime.parse("2023-06-20T15:00"),
      LocalDateTime.parse("2023-06-20T17:00"));

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldCheckThatTimePeriodIsNotAvailable() {
    repository.save(show);

    var result = repository.isPeriodAvailable(room,
        Range.closed(LocalDateTime.parse("2023-06-20T13:00"),
            LocalDateTime.parse("2023-06-20T16:00")));

    assertFalse(result);
  }

  @Test
  void shouldFindEventsForGivenPeriod() {
    repository.save(show);

    var result = repository.getForPeriod(room,
        Range.closed(LocalDateTime.parse("2023-06-20T16:00"),
            LocalDateTime.parse("2023-06-20T16:30")));

    assertEquals(1, result.size());

    assertEquals(showId, result.get(0).id());
  }

  @Test
  void shouldNotFindEventsForGivenPeriod() {
    repository.save(show);

    var result = repository.getForPeriod(room,
        Range.closed(LocalDateTime.parse("2023-06-20T12:00"),
            LocalDateTime.parse("2023-06-20T12:30")));

    assertTrue(result.isEmpty());
  }

}
