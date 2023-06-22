package com.radbrackets.cinema.event.show;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.radbrackets.cinema.config.CinemaConfig.OPENING_HOURS;
import static com.radbrackets.cinema.config.CinemaConfig.PREMIERE_HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddCinemaShowQueryTest {

  @Test
  void shouldValidatePremiereHours() {
    var error =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new AddCinemaShowQuery(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ShowType.PREMIERE,
                        false,
                        LocalDateTime.parse("2022-06-20T12:00"),
                        Duration.ofHours(2))
                    .validate(),
            "IllegalArgumentException error was expected");
    assertEquals(
        "Premiere movie show must start between %s and %s."
            .formatted(PREMIERE_HOURS.lowerEndpoint(), PREMIERE_HOURS.upperEndpoint()),
        error.getMessage());
  }

  @Test
  void shouldValidateOpeningHours() {
    var error =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new AddCinemaShowQuery(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    ShowType.PREMIERE,
                    false,
                    LocalDateTime.parse("2022-06-20T23:00"),
                    Duration.ofHours(2))
                    .validate(),
            "IllegalArgumentException error was expected");
    assertEquals(
        "Movie show must start between %s and %s."
            .formatted(OPENING_HOURS.lowerEndpoint(), OPENING_HOURS.upperEndpoint()),
        error.getMessage());
  }
}
