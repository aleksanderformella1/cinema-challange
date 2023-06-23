package com.radbrackets.cinema.event.room;

import com.radbrackets.cinema.event.GetEventQuery;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.radbrackets.cinema.event.room.RoomEventType.UNAVAILABILITY;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FindRoomEventServiceTest {

  @Autowired private RoomEventRepository roomEventRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private FindRoomEventService service;

  private static final List<Room> roomsCatalog =
      List.of(
          new Room(randomUUID(), "Room 1", Duration.ofMinutes(5)),
          new Room(randomUUID(), "Room 2", Duration.ofMinutes(15)),
          new Room(randomUUID(), "Room 3", Duration.ofMinutes(35)));

  @BeforeEach
  void setUp() {
    roomRepository.deleteAll();
    roomEventRepository.deleteAll();
  }

  @Test
  void shouldFindEvents() {
    var firstRoom = roomsCatalog.get(0);
    var secondRoom = roomsCatalog.get(1);
    roomRepository.add(firstRoom);
    roomRepository.add(secondRoom);
    var firstEventStart = LocalDateTime.parse("2023-06-20T15:00");
    var secondEventStart = LocalDateTime.parse("2023-06-20T18:00");
    var eventDuration = Duration.ofMinutes(30);
    roomEventRepository.save(
        new RoomEvent(
            randomUUID(),
            firstRoom,
            firstEventStart,
            firstEventStart.plus(eventDuration),
            UNAVAILABILITY));
    roomEventRepository.save(
        new RoomEvent(
            randomUUID(),
            secondRoom,
            secondEventStart,
            secondEventStart.plus(eventDuration),
            UNAVAILABILITY));

    var firstRoomEvents =
        service.find(
            new GetEventQuery(
                firstRoom.id(), firstEventStart, firstEventStart.plus(eventDuration)));

    assertEquals(1, firstRoomEvents.size());
    var firstRoomEvent = firstRoomEvents.get(0);
    assertEquals(firstRoom, firstRoomEvent.room());
    assertEquals(firstEventStart, firstRoomEvent.start());

    var secondRoomEvents =
        service.find(
            new GetEventQuery(
                secondRoom.id(), secondEventStart, secondEventStart.plus(eventDuration)));

    assertEquals(1, secondRoomEvents.size());
    var secondRoomEvent = secondRoomEvents.get(0);
    assertEquals(secondRoom, secondRoomEvent.room());
    assertEquals(secondEventStart, secondRoomEvent.start());
  }

  @Test
  void shouldNotFindEventsForWrongTimePeriod() {
    var firstRoom = roomsCatalog.get(0);
    roomRepository.add(firstRoom);
    var firstEventStart = LocalDateTime.parse("2023-06-20T15:00");
    var eventDuration = Duration.ofMinutes(30);
    roomEventRepository.save(
        new RoomEvent(
            randomUUID(),
            firstRoom,
            firstEventStart,
            firstEventStart.plus(eventDuration),
            UNAVAILABILITY));

    var firstRoomEvents =
        service.find(
            new GetEventQuery(
                firstRoom.id(), firstEventStart.minusDays(2), firstEventStart.minusDays(1)));

    assertTrue(firstRoomEvents.isEmpty());
  }

  @Test
  void shouldNotFindEventsForWrongRoom() {
    var firstRoom = roomsCatalog.get(0);
    var secondRoom = roomsCatalog.get(1);
    roomRepository.add(firstRoom);
    roomRepository.add(secondRoom);
    var wrongId = roomsCatalog.get(1).id();
    var firstEventStart = LocalDateTime.parse("2023-06-20T15:00");
    var eventDuration = Duration.ofMinutes(30);
    roomEventRepository.save(
        new RoomEvent(
            randomUUID(),
            firstRoom,
            firstEventStart,
            firstEventStart.plus(eventDuration),
            UNAVAILABILITY));

    var firstRoomEvents =
        service.find(
            new GetEventQuery(
                wrongId, firstEventStart, firstEventStart.plus(eventDuration)));

    assertTrue(firstRoomEvents.isEmpty());
  }
}
