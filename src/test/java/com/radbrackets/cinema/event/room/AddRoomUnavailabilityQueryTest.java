package com.radbrackets.cinema.event.room;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.radbrackets.cinema.config.CinemaConfig.OPENING_HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddRoomUnavailabilityQueryTest {

  @Test
  void shouldValidateOpeningHours() {
    var error =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new AddRoomUnavailabilityQuery(
                    UUID.randomUUID(),
                    LocalDateTime.parse("2022-06-20T23:00"),
                    LocalDateTime.parse("2022-06-20T23:30"))
                    .validate(),
            "IllegalArgumentException error was expected");
    assertEquals(
        "Room unavailability must start between %s and %s."
            .formatted(OPENING_HOURS.lowerEndpoint(), OPENING_HOURS.upperEndpoint()),
        error.getMessage());
  }

  @Test
  void shouldValidateEventStartBeforeEnd() {
    var error =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new AddRoomUnavailabilityQuery(
                    UUID.randomUUID(),
                    LocalDateTime.parse("2022-06-20T23:30"),
                    LocalDateTime.parse("2022-06-20T23:00"))
                    .validate(),
            "IllegalArgumentException error was expected");
    assertEquals(
        "Time range beginning cannot be after the end.",
        error.getMessage());
  }
}
