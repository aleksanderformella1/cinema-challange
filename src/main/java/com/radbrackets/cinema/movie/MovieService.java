package com.radbrackets.cinema.movie;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository repository;

  public void addNew(String name) {
    repository.add(new Movie(randomUUID(), name));
  }

  public List<Movie> getAll() {
    return repository.getAll();
  }
}
