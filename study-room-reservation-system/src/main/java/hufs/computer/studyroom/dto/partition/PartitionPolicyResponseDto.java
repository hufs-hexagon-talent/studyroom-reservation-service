package hufs.computer.studyroom.dto.partition;

import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartitionPolicyResponseDto {
    private final Long roomPartitionId;
    private final Long roomId;
    private final String roomName;
    private final String partitionNumber;
    private final RoomOperationPolicy policy;

    public PartitionPolicyResponseDto(Long roomPartitionId, Long roomId, String roomName, String partitionNumber, RoomOperationPolicy policy) {
        this.roomPartitionId = roomPartitionId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.partitionNumber = partitionNumber;
        this.policy = policy;
    }
}
