package com.radbrackets.cinema.event.room;

import lombok.RequiredArgsConstructor;

import com.google.common.collect.Range;
import com.radbrackets.cinema.event.GetEventQuery;
import com.radbrackets.cinema.room.Room;
import com.radbrackets.cinema.room.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FindRoomEventService {
  private final RoomEventRepository roomEventRepository;
  private final RoomRepository roomRepository;

  public List<RoomEvent> find(GetEventQuery query) {
    query.validate();

    Room room = roomRepository.get(query.roomId());
    return roomEventRepository.getForPeriod(room, Range.closed(query.from(), query.to()));
  }
}
