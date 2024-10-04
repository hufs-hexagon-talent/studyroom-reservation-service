package hufs.computer.studyroom.domain.partition.dto;

import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
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
