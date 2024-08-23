package com.test.studyroomreservationsystem.dto.operationpolicyschedule;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class ScheduleRequestDto {
    private Long roomOperationPolicyId;
    private List<Long> roomIds;
    private List<LocalDate> policyApplicationDates; // KST


    public ScheduleRequestDto(Long roomOperationPolicyId,
                              List<Long> roomIds,
                              List<LocalDate> policyApplicationDates) {
        this.roomOperationPolicyId = roomOperationPolicyId;
        this.roomIds = roomIds;
        this.policyApplicationDates = policyApplicationDates;
    }

    public RoomOperationPolicySchedule toEntity(RoomOperationPolicy roomOperationPolicy,
                                                Room room ,
                                                LocalDate policyApplicationDate) {
        return RoomOperationPolicySchedule.builder()
                .roomOperationPolicy(roomOperationPolicy)
                .room(room)
                .policyApplicationDate(policyApplicationDate)
                .build();
    }
}
