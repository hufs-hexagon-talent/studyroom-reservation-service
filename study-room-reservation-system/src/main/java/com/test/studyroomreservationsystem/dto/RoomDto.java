package com.test.studyroomreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RoomDto {
    private Long roomId;
    private String roomName;
    private Long roomOperationPolicyId;
}
