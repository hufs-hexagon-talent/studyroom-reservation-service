package hufs.computer.studyroom.dto.partition;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PartitionUpdateRequestDto {
    private Long roomId;
    private String partitionNumber;

    @Builder
    public PartitionUpdateRequestDto(Long roomId, String partitionNumber) {
        this.roomId = roomId;
        this.partitionNumber = partitionNumber;
    }
    // from :  Entity -> Dto



    }
