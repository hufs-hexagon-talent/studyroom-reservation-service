package com.test.studyroomreservationsystem.dto.operationpolicyschedule;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoomOperationPolicyScheduleUpdateDto {
    private Long roomId;
    private Long roomOperationPolicyId;
    private LocalDate policyApplicationDate;

}
