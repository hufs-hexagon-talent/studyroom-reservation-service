package com.test.studyroomreservationsystem.dto.roomoperationpolicy;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter@Setter
public class RoomOperationPolicyDto {
    private LocalTime operationStartTime;
    private LocalTime operationEndTime;
    private Integer eachMaxMinute;
}
