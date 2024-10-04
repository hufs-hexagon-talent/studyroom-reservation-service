package hufs.computer.studyroom.domain.room.service;

import hufs.computer.studyroom.domain.room.dto.RoomDto;
import hufs.computer.studyroom.domain.room.dto.RoomResponseDto;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.dto.RoomUpdateRequestDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    Room createRoom(RoomDto roomDto);
    Room findRoomById(Long roomId);
    Room findRoomByName(String roomName);
    List<Room> findAllRoom();
    Room updateRoom(Long roomId, RoomUpdateRequestDto roomUpdateDto);
    void deleteRoom(Long roomId);
    void isRoomAvailable(Long roomId, Instant startDateTime, Instant endDateTime);
    List<RoomResponseDto> getRoomsPolicyByDate(LocalDate date);

    default RoomDto dtoFrom(Room room) {
        return RoomDto.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .build();
    }
}
