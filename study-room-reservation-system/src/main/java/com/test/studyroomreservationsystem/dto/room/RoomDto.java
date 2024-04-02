package com.test.studyroomreservationsystem.dto.room;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RoomDto {
    private Long roomId;
    private String roomName;
}
