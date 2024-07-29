package com.test.studyroomreservationsystem.dto.partition;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.domain.entity.User;
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
