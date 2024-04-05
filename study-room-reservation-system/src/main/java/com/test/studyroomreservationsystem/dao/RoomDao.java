package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomDao {
    Room save(Room room);
    Optional<Room> findById(Long roomId);
    Optional<Room> findByRoomName(String roomName);
    List<Room> findAll();
    void deleteById(Long roomId);


}
