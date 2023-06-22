package com.radbrackets.cinema.event.room;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.radbrackets.cinema.config.CinemaConfig.OPENING_HOURS;

@Getter
public class AddRoomUnavailabilityQuery extends RoomEventQuery {

  public AddRoomUnavailabilityQuery(
      UUID roomId, LocalDateTime from, LocalDateTime to) {
    super(roomId, from, to, RoomEventType.UNAVAILABILITY);
  }

  public void validate() {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Time range beginning cannot be after the end.");
    }

    if (!OPENING_HOURS.contains(start.toLocalTime())) {
      throw new IllegalArgumentException(
          "Room unavailability must start between %s and %s."
              .formatted(OPENING_HOURS.lowerEndpoint(), OPENING_HOURS.upperEndpoint()));
    }
  }
}
