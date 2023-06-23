package com.radbrackets.cinema.room;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
class RoomController {

  private final FindRoomService service;

  @GetMapping
  public List<Room> getAll() {
    return service.getAll();
  }
}
