package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.dto.partition.PartitionPolicyResponseDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionRequestDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomPartitionService {
    RoomPartition createRoomPartition(PartitionRequestDto roomPartitionDto );
    RoomPartition findRoomPartitionById(Long roomPartitionId);
    List<RoomPartition> findRoomPartitionsByRoomId(Long roomId);
    List<RoomPartition> findAllRoomPartition();
    List<RoomPartition> findRoomPartitionByRoomId(Long roomId);
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
