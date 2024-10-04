package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.dto.PartitionPolicyResponseDto;
import hufs.computer.studyroom.domain.partition.dto.PartitionRequestDto;
import hufs.computer.studyroom.domain.partition.dto.PartitionResponseDto;
import hufs.computer.studyroom.domain.partition.dto.PartitionUpdateRequestDto;

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
