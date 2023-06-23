package com.radbrackets.cinema.event;

import lombok.Getter;

import com.google.common.collect.Range;
import com.radbrackets.cinema.room.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventRepository<TEvent extends Event> {

  @Getter
  private final List<TEvent> events = new ArrayList<>();

  public void save(TEvent event) {
    events.add(event);
  }

  public List<TEvent> getForPeriod(Room room, Range<LocalDateTime> period) {
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
