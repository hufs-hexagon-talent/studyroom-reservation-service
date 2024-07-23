package com.test.studyroomreservationsystem.dto.partition;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartitionResponseDto {
    private Long roomPartitionId;
    private Long roomId;
    private String roomName;
    private String partitionNumber;

    public PartitionResponseDto(Long roomPartitionId, Long roomId, String roomName, String partitionNumber) {
        this.roomPartitionId = roomPartitionId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.partitionNumber = partitionNumber;
    }
}
