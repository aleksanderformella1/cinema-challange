package com.radbrackets.cinema.show;

import com.radbrackets.cinema.event.room.RoomEventRepository;
import com.radbrackets.cinema.event.room.RoomEventType;
import com.radbrackets.cinema.event.show.*;
import com.radbrackets.cinema.movie.Movie;
import com.radbrackets.cinema.movie.MovieRepository;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.radbrackets.cinema.config.CinemaConfig.OPENING_HOURS;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CinemaShowServiceTest {

  @Autowired private RoomEventRepository roomEventRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private CinemaShowRepository cinemaShowRepository;

  @Autowired private CinemaShowService service;

  private final List<Movie> movieCatalog =
      List.of(
          new Movie(randomUUID(), "Shrek"),
          new Movie(randomUUID(), "Godfather"),
          new Movie(randomUUID(), "Casablanca"));

  private final List<Room> roomsCatalog =
      List.of(
          new Room(randomUUID(), "Room 1", Duration.ofMinutes(5)),
          new Room(randomUUID(), "Room 2", Duration.ofMinutes(15)),
          new Room(randomUUID(), "Room 3", Duration.ofMinutes(35)));

  @BeforeAll
  void setUp() {
    movieCatalog.forEach(movieRepository::add);
    roomsCatalog.forEach(roomRepository::add);
  }

  @BeforeEach
  void prepareTest() {
    roomEventRepository.deleteAll();
    cinemaShowRepository.deleteAll();
  }

  @Test
  void shouldAddNewShow() {
    var room = roomsCatalog.get(0);
    var movie = movieCatalog.get(0);
    var showStart = LocalDateTime.parse("2023-06-20T15:00");
    var showDuration = Duration.ofHours(2);
    var query =
        new AddCinemaShowQuery(
            room.id(), movie.id(), ShowType.REGULAR, false, showStart, showDuration);

    service.addNew(query);

    var createdShows = cinemaShowRepository.getEvents();
    assertEquals(1, createdShows.size());
    var createdShow = createdShows.get(0);
    assertEquals(movie, createdShow.movie());
    assertEquals(room, createdShow.room());
    assertEquals(showStart, createdShow.start());
    assertEquals(showStart.plus(showDuration), createdShow.end());

    var createdRoomsCleanups = roomEventRepository.getEvents();
    assertEquals(1, createdRoomsCleanups.size());
    var createdRoomCleanup = createdRoomsCleanups.get(0);
    assertEquals(room, createdRoomCleanup.room());
    assertEquals(RoomEventType.CLEANING_SLOT, createdRoomCleanup.eventType());
  }

  @Test
  void shouldNotAddTheSameShowTwice() {
    var room = roomsCatalog.get(0);
    var movie = movieCatalog.get(0);
    var showStart = LocalDateTime.parse("2023-06-20T15:00");
    var showDuration = Duration.ofHours(2);
    var query =
        new AddCinemaShowQuery(
            room.id(), movie.id(), ShowType.REGULAR, false, showStart, showDuration);

    service.addNew(query);

    var error =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addNew(query),
            "IllegalArgumentException error was expected");

    assertEquals(
        "Time slot between 2023-06-20T15:00 and 2023-06-20T17:00 is not available.",
        error.getMessage());
  }

  @Test
  void shouldValidateQuery() {
    var room = roomsCatalog.get(0);
    var movie = movieCatalog.get(0);
    var showStart = LocalDateTime.parse("2023-06-20T23:00");
    var showDuration = Duration.ofHours(2);
    var query =
        new AddCinemaShowQuery(
            room.id(), movie.id(), ShowType.REGULAR, false, showStart, showDuration);

    var error =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addNew(query),
            "IllegalArgumentException error was expected");
    assertEquals("Movie show must start between %s and %s."
        .formatted(OPENING_HOURS.lowerEndpoint(), OPENING_HOURS.upperEndpoint()),
        error.getMessage());
  }
}
