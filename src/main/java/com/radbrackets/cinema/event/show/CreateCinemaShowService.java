package com.radbrackets.cinema.event.show;

import lombok.RequiredArgsConstructor;

import com.google.common.collect.Range;
import com.radbrackets.cinema.event.room.CreateRoomEventService;
import com.radbrackets.cinema.event.room.RoomEventQuery;
import com.radbrackets.cinema.event.room.RoomEventType;
import com.radbrackets.cinema.movie.Movie;
import com.radbrackets.cinema.movie.MovieRepository;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
class CreateCinemaShowService {

  private final CinemaShowRepository cinemaShowRepository;
  private final MovieRepository movieRepository;
  private final RoomRepository roomRepository;
  private final CreateRoomEventService createRoomEventService;


  //NOTE should be transactional in the real example - room cleanup must be always created with the show
  public synchronized void addNew(AddCinemaShowQuery query) {
    query.validate();

    Range<LocalDateTime> showTimeRange =
        Range.closed(query.start(), query.start().plus(query.duration()));
    Room room = roomRepository.get(query.roomId());

    if (!cinemaShowRepository.isPeriodAvailable(room, showTimeRange)) {
      throw new IllegalArgumentException(
          "Time slot between %s and %s is not available."
              .formatted(showTimeRange.lowerEndpoint(), showTimeRange.upperEndpoint()));
    }
    Movie movie = movieRepository.get(query.movieId());

    cinemaShowRepository.save(
        new CinemaShow(
            randomUUID(),
            movie,
            room,
            query.showType(),
            showTimeRange.lowerEndpoint(),
            showTimeRange.upperEndpoint()));

    createRoomEventService.addNew(
        new RoomEventQuery(room.id(),
            showTimeRange.upperEndpoint(),
            showTimeRange.upperEndpoint().plus(room.cleaningTime()),
            RoomEventType.CLEANING_SLOT));
  }
}
