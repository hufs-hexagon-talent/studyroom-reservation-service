package hufs.computer.studyroom.dto.partition;

import hufs.computer.studyroom.domain.entity.Room;
import hufs.computer.studyroom.domain.entity.RoomPartition;
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
