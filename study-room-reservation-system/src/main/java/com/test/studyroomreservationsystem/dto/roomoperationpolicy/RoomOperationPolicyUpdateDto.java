package com.test.studyroomreservationsystem.dto.roomoperationpolicy;

import lombok.Getter;

import java.time.LocalTime;
@Getter
public class RoomOperationPolicyUpdateDto {
    private LocalTime operationStartTime;
    private LocalTime operationEndTime;
    private Integer eachMaxMinute;
}
