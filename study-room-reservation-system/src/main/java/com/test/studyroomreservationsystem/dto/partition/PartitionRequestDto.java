package com.test.studyroomreservationsystem.dto.partition;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartitionRequestDto {
    private Long roomPartitionId;
    private Long roomId;
    private String partitionNumber;

    public PartitionRequestDto(Long roomPartitionId, Long roomId, String partitionNumber) {
        this.roomPartitionId = roomPartitionId;
        this.roomId = roomId;
        this.partitionNumber = partitionNumber;
    }
    public RoomPartition toEntity(Room room){
        return RoomPartition.builder()
                .roomPartitionId(roomPartitionId)
                .room(room)
                .partitionNumber(partitionNumber)
                .build();
    }
}
