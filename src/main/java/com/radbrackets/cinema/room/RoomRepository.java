package com.radbrackets.cinema.room;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RoomRepository {

  private static final Map<UUID, Room> rooms = Maps.newHashMap();

  public List<Room> getAll() {
    return new ArrayList<>(rooms.values());
  }

  public Room get(UUID id) {
    if (!rooms.containsKey(id)) {
      throw new IllegalArgumentException("Room of ID %s does not exists.".formatted(id));
    }
    return rooms.get(id);
  }

  public void add(Room room) {
    UUID id = room.id();
    if (rooms.containsKey(id)) {
      throw new IllegalArgumentException("Room of ID %s already exists.".formatted(id));
    }
    rooms.put(id, room);
  }

  public void deleteAll() {
    rooms.clear();
  }
}
