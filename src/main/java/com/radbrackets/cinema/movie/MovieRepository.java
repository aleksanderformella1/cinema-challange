package com.radbrackets.cinema.movie;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MovieRepository {
  private static final Map<UUID, Movie> movies = new HashMap<>();

  public List<Movie> getAll() {
    return new ArrayList<>(movies.values());
  }

  public Movie get(UUID id) {
    if (!movies.containsKey(id)) {
      throw new IllegalArgumentException("Movie of ID %s does not exists.".formatted(id));
    }
    return movies.get(id);
  }

  public void add(Movie movie) {
    UUID id = movie.id();
    if (movies.containsKey(id)) {
      throw new IllegalArgumentException("Movie of ID %s already exists.".formatted(id));
    }
    movies.put(id, movie);
  }
}
