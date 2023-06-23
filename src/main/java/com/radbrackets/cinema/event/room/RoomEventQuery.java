package com.radbrackets.cinema.event.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.radbrackets.cinema.event.EventQuery;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class RoomEventQuery implements EventQuery {
  protected final UUID roomId;
  protected final LocalDateTime start;
  protected final LocalDateTime end;
  protected final RoomEventType eventType;

  public void validate() {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Time range beginning cannot be after the end.");
    }
  }
}
