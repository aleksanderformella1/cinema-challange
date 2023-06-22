package com.radbrackets.cinema.room;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
@Service
public class RoomService {

  private final RoomRepository roomRepository;

  public void addNew(String name, Duration cleaningTime) {
    roomRepository.add(new Room(randomUUID(), name, cleaningTime));
  }

  public List<Room> getAll() {
    return roomRepository.getAll();
  }
}
