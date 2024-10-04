package hufs.computer.studyroom.service;

import hufs.computer.studyroom.domain.entity.RoomPartition;
import hufs.computer.studyroom.dto.partition.PartitionPolicyResponseDto;
import hufs.computer.studyroom.dto.partition.PartitionRequestDto;
import hufs.computer.studyroom.dto.partition.PartitionResponseDto;
import hufs.computer.studyroom.dto.partition.PartitionUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomPartitionService {
    RoomPartition createRoomPartition(PartitionRequestDto roomPartitionDto );
    RoomPartition findRoomPartitionById(Long roomPartitionId);
    List<RoomPartition> findRoomPartitionsByRoomId(Long roomId);
    List<RoomPartition> findAllRoomPartition();
    List<RoomPartition> findRoomPartitionByRoomId(Long roomId);
    RoomPartition updateRoomPartition(Long partitionId, PartitionUpdateRequestDto updateRequestDto);
    void deletePartitionById(Long roomPartitionId);
    List<PartitionPolicyResponseDto> getPartitionsPolicyByDate(LocalDate date);

    default PartitionResponseDto dtoFrom(RoomPartition roomPartition) {
        return PartitionResponseDto.builder()
                .roomPartitionId(roomPartition.getRoomPartitionId())
                .roomId(roomPartition.getRoom().getRoomId())
                .roomName(roomPartition.getRoom().getRoomName())
                .partitionNumber(roomPartition.getPartitionNumber())
                .build();
    }
}
