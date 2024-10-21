package hufs.computer.studyroom.domain.room.service;

import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.mapper.RoomPartitionMapper;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponse;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponses;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.mapper.RoomMapper;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomQueryService {
    private final RoomRepository roomRepository;
    private final RoomPartitionRepository partitionRepository;
    private final RoomPartitionMapper partitionMapper;
    private final RoomMapper roomMapper;

    public RoomInfoResponse findRoomById(Long roomId) {
        Room room = getRoomById(roomId);
        return roomMapper.toInfoResponse(room);
    }

    public RoomInfoResponses findAllRoom() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.toRoomInfoResponses(rooms);
    }


    public PartitionInfoResponses findPartitionsByRoomId(Long roomId) {
        List<RoomPartition> partitions = partitionRepository.findByRoom_RoomId(roomId);
        return partitionMapper.toPartitionInfoResponses(partitions);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
    }
    public boolean existByRoomId(Long roomId) {
        return roomRepository.existsById(roomId);
    }

}
