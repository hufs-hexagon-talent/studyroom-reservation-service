package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.dto.reservation.RoomsReservationResponseDto;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;
import com.test.studyroomreservationsystem.dto.room.RoomsResponseDto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface RoomService {
    Room createRoom(RoomDto roomDto);
    Room findRoomById(Long roomId);
    Room findRoomByName(String roomName);
    List<Room> findAllRoom();
    Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);
    void deleteRoom(Long roomId);
    void isRoomAvailable(Long roomId, ZonedDateTime startDateTime, ZonedDateTime endDateTime);
    List<RoomsReservationResponseDto> getRoomsReservationsByDate(LocalDate date);
    List<RoomsResponseDto> getRoomsPolicyByDate(LocalDate date);


    default  RoomDto dtoFrom(Room room) {
        return RoomDto.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .build();
    }
}
