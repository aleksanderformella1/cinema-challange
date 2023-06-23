package com.radbrackets.cinema.room;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class FindRoomService {

  private final RoomRepository roomRepository;

  public List<Room> getAll() {
    return roomRepository.getAll();
  }
}
