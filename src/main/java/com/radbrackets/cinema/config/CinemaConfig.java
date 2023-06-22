package com.radbrackets.cinema.config;

import com.google.common.collect.Range;

import java.time.LocalTime;

public class CinemaConfig {
  public static final Range<LocalTime> OPENING_HOURS = Range.closed(
      LocalTime.of(8, 0), LocalTime.of(22, 0)
  );

  public static final Range<LocalTime> PREMIERE_HOURS = Range.closed(
      LocalTime.of(17, 0), LocalTime.of(21, 0)
  );
}
