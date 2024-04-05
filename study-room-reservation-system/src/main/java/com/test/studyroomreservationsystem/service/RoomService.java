package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {
    Room createRoom(RoomDto roomDto);
    Room findRoomById(Long roomId);
    Room findRoomByName(String roomName);
    List<Room> findAllRoom();
    Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);
    void deleteRoom(Long roomId);
    boolean isRoomAvailable(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime);


}
