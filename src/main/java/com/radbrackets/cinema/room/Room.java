package com.radbrackets.cinema.room;

import java.time.Duration;
import java.util.UUID;

public record Room(UUID id, String name, Duration cleaningTime) {
}
