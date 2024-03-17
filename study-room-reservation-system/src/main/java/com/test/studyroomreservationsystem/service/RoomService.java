package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.dto.ReservationDto;
import com.test.studyroomreservationsystem.dto.RoomDto;
import com.test.studyroomreservationsystem.dto.RoomUpdateDto;

import java.util.List;

public interface RoomService {
    Room createRoom(RoomDto roomDto);
    Room findRoomById(Long roomId);
    Room findRoomByName(String roomName);
    List<Room> findAllRoom();
    Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);
    void deleteRoom(Long roomId);
    RoomDto convertToDto(Room room);
    boolean isRoomAvailable(Long roomId, ReservationDto createDto);
}
