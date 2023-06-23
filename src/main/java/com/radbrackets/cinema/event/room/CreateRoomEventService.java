package com.radbrackets.cinema.event.room;

import lombok.RequiredArgsConstructor;

import com.google.common.collect.Range;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class CreateRoomEventService {

  private final RoomEventRepository roomEventRepository;
  private final RoomRepository roomRepository;

  public synchronized void addNew(RoomEventQuery query) {
    query.validate();

    Range<LocalDateTime> showTimeRange = Range.closed(query.getStart(), query.getEnd());
    Room room = roomRepository.get(query.roomId);

    if (!roomEventRepository.isPeriodAvailable(room, showTimeRange)) {
      throw new IllegalArgumentException(
          "Time slot between %s and %s is not available."
              .formatted(showTimeRange.lowerEndpoint(), showTimeRange.upperEndpoint()));
    }

    roomEventRepository.save(
        new RoomEvent(randomUUID(), room, query.getStart(), query.getEnd(), query.getEventType()));
  }

}
