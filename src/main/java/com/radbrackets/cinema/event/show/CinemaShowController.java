package com.radbrackets.cinema.event.show;

import lombok.AllArgsConstructor;

import com.radbrackets.cinema.event.GetEventQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event/show")
@AllArgsConstructor
class CinemaShowController {

  private final CinemaShowService service;

  @PostMapping
  public void add(@RequestBody AddCinemaShowQuery query) {
    service.addNew(query);
  }

  @GetMapping
  public List<CinemaShow> find(GetEventQuery query) {
    return service.find(query);
  }
}
