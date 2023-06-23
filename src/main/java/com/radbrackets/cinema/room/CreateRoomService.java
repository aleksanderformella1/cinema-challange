package com.radbrackets.cinema.room;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Duration;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
@Service
public class CreateRoomService {

  private final RoomRepository roomRepository;

  public void addNew(String name, Duration cleaningTime) {
    roomRepository.add(new Room(randomUUID(), name, cleaningTime));
  }
}
