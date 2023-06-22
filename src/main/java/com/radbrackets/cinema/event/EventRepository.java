package com.radbrackets.cinema.event;

import lombok.Getter;

import com.google.common.collect.Range;
import com.radbrackets.cinema.room.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventRepository<TRoomEvent extends Event> {

  @Getter
  private final List<TRoomEvent> events = new ArrayList<>();

  public void save(TRoomEvent event) {
    events.add(event);
  }

  public List<TRoomEvent> getForPeriod(Room room, Range<LocalDateTime> period) {
    return getEvents().stream()
        .filter(event -> event.room().id().equals(room.id()) &&
            period.isConnected(Range.closed(event.start(), event.end())))
        .collect(Collectors.toList());
  }

  public boolean isPeriodAvailable(Room room, Range<LocalDateTime> period) {
    return getForPeriod(room, period).isEmpty();
  }

  public void deleteAll() {
    events.clear();
  }
}
