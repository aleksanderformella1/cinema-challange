package com.radbrackets.cinema.event;

import com.radbrackets.cinema.room.Room;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Event extends Comparable<LocalDateTime> {

  UUID id();
  Room room();
  LocalDateTime start();
  LocalDateTime end();

  @Override
  default int compareTo(LocalDateTime event) {
    return start().compareTo(event);
  }
}
