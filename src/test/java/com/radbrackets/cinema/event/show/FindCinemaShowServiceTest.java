package com.radbrackets.cinema.event.show;

import com.radbrackets.cinema.event.GetEventQuery;
import com.radbrackets.cinema.event.room.RoomEventRepository;
import com.radbrackets.cinema.movie.Movie;
import com.radbrackets.cinema.movie.MovieRepository;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCinemaShowServiceTest {

  @Autowired private CinemaShowRepository cinemaShowRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private RoomEventRepository roomEventRepository;

  @Autowired private FindCinemaShowService service;

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
  void shouldFindShow() {
    var room = roomsCatalog.get(0);
    var movie = movieCatalog.get(0);
    var firstShowStart = LocalDateTime.parse("2023-06-20T15:00");
    var secondShowStart = firstShowStart.plusDays(1);
    var showDuration = Duration.ofHours(2);
    CinemaShow firstShow = new CinemaShow(randomUUID(), movie, room, ShowType.REGULAR, firstShowStart,
        firstShowStart.plus(showDuration));
    CinemaShow secondShow = new CinemaShow(randomUUID(), movie, room, ShowType.REGULAR, secondShowStart,
        secondShowStart.plus(showDuration));
    cinemaShowRepository.save(firstShow);
    cinemaShowRepository.save(secondShow);

    List<CinemaShow> result =
        service
            .find(new GetEventQuery(room.id(), firstShowStart, secondShowStart.plus(showDuration)))
            .stream().sorted().toList();

    assertEquals(2, result.size());
    assertEquals(firstShow, result.get(0));
    assertEquals(secondShow, result.get(1));
  }

  @Test
  void shouldFindNothingForWrongTimeRange() {
    var room = roomsCatalog.get(0);
    var movie = movieCatalog.get(0);
    var showStart = LocalDateTime.parse("2023-06-20T15:00");
    var showDuration = Duration.ofHours(2);
    CinemaShow show = new CinemaShow(randomUUID(), movie, room, ShowType.REGULAR, showStart,
        showStart.plus(showDuration));
    cinemaShowRepository.save(show);

    List<CinemaShow> result =
        service.find(new GetEventQuery(room.id(), showStart.minusDays(2), showStart.minusDays(1)));

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFindNothingForWrongRoom() {
    var wrongRoomId = roomsCatalog.get(1).id();
    var room = roomsCatalog.get(0);
    var movie = movieCatalog.get(0);
    var showStart = LocalDateTime.parse("2023-06-20T15:00");
    var showDuration = Duration.ofHours(2);
    CinemaShow show = new CinemaShow(randomUUID(), movie, room, ShowType.REGULAR, showStart,
        showStart.plus(showDuration));
    cinemaShowRepository.save(show);

    List<CinemaShow> result =
        service.find(new GetEventQuery(wrongRoomId, showStart, showStart.plus(showDuration)));

    assertTrue(result.isEmpty());
  }
}
