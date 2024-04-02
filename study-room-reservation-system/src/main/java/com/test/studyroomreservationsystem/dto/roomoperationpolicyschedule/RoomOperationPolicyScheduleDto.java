package com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter@Setter
public class RoomOperationPolicyScheduleDto {
    private Long roomOperationPolicyScheduleId;
    private Room room;
    private RoomOperationPolicy roomOperationPolicy;
    private LocalDate policyApplicationDate;

}
