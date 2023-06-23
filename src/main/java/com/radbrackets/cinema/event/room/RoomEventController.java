package com.radbrackets.cinema.event.room;

import lombok.RequiredArgsConstructor;

import com.radbrackets.cinema.event.GetEventQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event/room")
@RequiredArgsConstructor
class RoomEventController {

  private final CreateRoomEventService createRoomEventService;
  private final FindRoomEventService findRoomEventService;

  @PostMapping("/unavailability")
  public void add(@RequestBody AddRoomUnavailabilityQuery query) {
    createRoomEventService.addNew(query);
  }

  @GetMapping
  public List<RoomEvent> find(GetEventQuery query) {
    return findRoomEventService.find(query);
  }
}
