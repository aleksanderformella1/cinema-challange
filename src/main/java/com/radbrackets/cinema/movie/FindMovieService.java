package com.radbrackets.cinema.movie;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FindMovieService {

  private final MovieRepository repository;

  public List<Movie> getAll() {
    return repository.getAll();
  }
}
