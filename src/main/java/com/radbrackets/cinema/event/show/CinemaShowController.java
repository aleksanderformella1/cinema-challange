package com.radbrackets.cinema.event.show;

import lombok.AllArgsConstructor;

import com.radbrackets.cinema.event.GetEventQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event/show")
@AllArgsConstructor
class CinemaShowController {

  private final CreateCinemaShowService createCinemaShowService;
  private final FindCinemaShowService findCinemaShowService;

  @PostMapping
  public void add(@RequestBody AddCinemaShowQuery query) {
    createCinemaShowService.addNew(query);
  }

  @GetMapping
  public List<CinemaShow> find(GetEventQuery query) {
    return findCinemaShowService.find(query);
  }
}
