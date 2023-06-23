package com.radbrackets.cinema.event.room;

import com.google.common.collect.Range;
import com.radbrackets.cinema.event.GetEventQuery;
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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.radbrackets.cinema.event.room.RoomEventType.UNAVAILABILITY;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateRoomEventServiceTest {

  @Autowired private RoomEventRepository roomEventRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private CreateRoomEventService service;

  private static final List<Room> roomsCatalog =
      List.of(
          new Room(randomUUID(), "Room 1", Duration.ofMinutes(5)),
          new Room(randomUUID(), "Room 2", Duration.ofMinutes(15)),
          new Room(randomUUID(), "Room 3", Duration.ofMinutes(35)));

  @BeforeAll
  void init() {
    roomsCatalog.forEach(roomRepository::add);
  }

  @BeforeEach
  void setUp() {
    roomRepository.deleteAll();
    roomEventRepository.deleteAll();
  }

  @Test
  void shouldAddNewEvent() {
    var room = roomsCatalog.get(0);
    roomRepository.add(room);
    var start = LocalDateTime.parse("2023-06-20T15:00");
    var end = LocalDateTime.parse("2023-06-20T18:00");
    var eventQuery = new RoomEventQuery(room.id(), start, end, UNAVAILABILITY);
    service.addNew(eventQuery);

    var roomEvents = roomEventRepository.getForPeriod(room, Range.closed(start, end));

    assertEquals(1, roomEvents.size());
    var roomEvent = roomEvents.get(0);
    assertEquals(room, roomEvent.room());
    assertEquals(start, roomEvent.start());
    assertEquals(end, roomEvent.end());
  }

  @Test
  public void testSynchronizedAddingNewEvent() throws InterruptedException {
    int numberOfThreads = 10;
    var executor = Executors.newFixedThreadPool(numberOfThreads);

    var room = roomsCatalog.get(0);
    roomRepository.add(room);
    var start = LocalDateTime.parse("2023-06-20T08:00");

    IntStream.range(0, numberOfThreads).forEach(
        i -> executor.submit(() -> {
              var query =
                  new RoomEventQuery(room.id(),
                      start.plusHours(i),
                      start.plusHours(i).plusMinutes(30),
                      UNAVAILABILITY);
              service.addNew(query);
            }
        ));
    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.SECONDS);
    assertEquals(numberOfThreads, roomEventRepository.getEvents().size());
  }

  @Test
  void shouldNotAddTheSameEventTwice() {
    var room = roomsCatalog.get(0);
    roomRepository.add(room);
    var start = LocalDateTime.parse("2023-06-20T15:00");
    var end = LocalDateTime.parse("2023-06-20T18:00");
    var eventQuery = new RoomEventQuery(room.id(), start, end, UNAVAILABILITY);

    service.addNew(eventQuery);
    var error =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addNew(eventQuery),
            "IllegalArgumentException error was expected");

    assertEquals(
        "Time slot between 2023-06-20T15:00 and 2023-06-20T18:00 is not available.",
        error.getMessage());
  }

  @Test
  void shouldValidateQuery() {
    var start = LocalDateTime.parse("2023-06-20T15:00");
    var end = LocalDateTime.parse("2023-06-20T18:00");
    var eventQuery = new RoomEventQuery(randomUUID(), end, start, UNAVAILABILITY);

    var error =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addNew(eventQuery),
            "IllegalArgumentException error was expected");
    assertEquals("Time range beginning cannot be after the end.", error.getMessage());
  }
}
