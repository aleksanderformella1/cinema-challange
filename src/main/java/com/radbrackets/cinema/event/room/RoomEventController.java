package com.radbrackets.cinema.event.room;

import lombok.RequiredArgsConstructor;

import com.radbrackets.cinema.event.GetEventQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event/room")
@RequiredArgsConstructor
class RoomEventController {

  private final RoomEventService service;

  @PostMapping("/unavailability")
  public void add(@RequestBody AddRoomUnavailabilityQuery query) {
    service.addNew(query);
  }

  @GetMapping
  public List<RoomEvent> find(GetEventQuery query) {
    return service.find(query);
  }
}
