package com.radbrackets.cinema.event.show;

import lombok.RequiredArgsConstructor;

import com.google.common.collect.Range;
import com.radbrackets.cinema.event.GetEventQuery;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FindCinemaShowService {

  private final CinemaShowRepository cinemaShowRepository;
  private final RoomRepository roomRepository;

  public List<CinemaShow> find(GetEventQuery query) {
    query.validate();

    Room room = roomRepository.get(query.roomId());
    return cinemaShowRepository.getForPeriod(room, Range.closed(query.from(), query.to()));
  }
}
