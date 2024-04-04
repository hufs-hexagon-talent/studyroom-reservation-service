package com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter@Setter
public class RoomOperationPolicyScheduleDto {
    private Long roomId;
    private Long roomOperationPolicyId;
    private LocalDate policyApplicationDate;

}
