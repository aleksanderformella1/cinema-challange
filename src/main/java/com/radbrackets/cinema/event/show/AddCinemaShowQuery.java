package com.radbrackets.cinema.event.show;

import com.radbrackets.cinema.event.EventQuery;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.radbrackets.cinema.config.CinemaConfig.OPENING_HOURS;
import static com.radbrackets.cinema.config.CinemaConfig.PREMIERE_HOURS;
import static com.radbrackets.cinema.event.show.ShowType.PREMIERE;

public record AddCinemaShowQuery(
    UUID roomId,
    UUID movieId,
    ShowType showType,
    boolean threeDimGlassesRequired,
    LocalDateTime start,
    Duration duration) implements EventQuery {

  public void validate() {
    if (!OPENING_HOURS.contains(start.toLocalTime())) {
      throw new IllegalArgumentException(
          "Movie show must start between %s and %s."
              .formatted(OPENING_HOURS.lowerEndpoint(), OPENING_HOURS.upperEndpoint()));
    }
    if (isPremiere() && !PREMIERE_HOURS.contains(start.toLocalTime())) {
      throw new IllegalArgumentException(
          "Premiere movie show must start between %s and %s."
              .formatted(PREMIERE_HOURS.lowerEndpoint(), PREMIERE_HOURS.upperEndpoint()));
    }
  }

  private boolean isPremiere() {
    return showType == PREMIERE;
  }
}
