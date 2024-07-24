package com.test.studyroomreservationsystem.dto.operationpolicy;

import lombok.Getter;

import java.time.LocalTime;
@Getter
public class RoomOperationPolicyUpdateDto {
    private LocalTime operationStartTime;
    private LocalTime operationEndTime;
    private Integer eachMaxMinute;
}
