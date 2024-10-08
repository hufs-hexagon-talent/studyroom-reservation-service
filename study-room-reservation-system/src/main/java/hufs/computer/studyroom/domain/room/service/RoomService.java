package hufs.computer.studyroom.domain.room.service;

import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.room.dto.request.CreateRoomRequest;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponse;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponses;
import hufs.computer.studyroom.domain.room.dto.request.ModifyRoomRequest;

import java.time.Instant;

public interface RoomService {
    RoomInfoResponse createRoom(CreateRoomRequest createRoomRequest);
    RoomInfoResponse findRoomById(Long roomId);
    RoomInfoResponses findAllRoom();
    RoomInfoResponse updateRoom(Long roomId, ModifyRoomRequest roomUpdateDto);
    PartitionInfoResponses findPartitionsByRoomId(Long roomId);
    void deleteRoom(Long roomId);
    void isRoomAvailable(Long roomId, Instant startDateTime, Instant endDateTime);

}
