package hufs.computer.studyroom.domain.partition.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartitionResponseDto {
    private final Long roomPartitionId;
    private final Long roomId;
    private final String roomName;
    private final String partitionNumber;

    public PartitionResponseDto(Long roomPartitionId, Long roomId, String roomName, String partitionNumber) {
        this.roomPartitionId = roomPartitionId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.partitionNumber = partitionNumber;
    }
}
