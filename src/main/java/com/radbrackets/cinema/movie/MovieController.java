package com.radbrackets.cinema.movie;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
class MovieController {

  private final MovieService service;

  @GetMapping
  public List<Movie> getAll() {
    return service.getAll();
  }
}
