package hufs.computer.studyroom.service;

import hufs.computer.studyroom.domain.entity.Room;
import hufs.computer.studyroom.dto.room.RoomDto;
import hufs.computer.studyroom.dto.room.RoomUpdateRequestDto;
import hufs.computer.studyroom.dto.room.RoomResponseDto;

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
