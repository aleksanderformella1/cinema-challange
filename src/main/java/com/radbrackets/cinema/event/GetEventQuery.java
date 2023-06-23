package com.radbrackets.cinema.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetEventQuery(
    UUID roomId, LocalDateTime from, LocalDateTime to) implements EventQuery {
  public void validate() {
    if (from.isAfter(to)) {
      throw new IllegalArgumentException("Time range beginning cannot be after the end.");
    }
  }
}
