package com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RoomOperationPolicyScheduleDto {
    private Long roomId;
    private Long roomOperationPolicyId;
    private LocalDate policyApplicationDate;

    @Builder
    public RoomOperationPolicyScheduleDto(Long roomId, Long roomOperationPolicyId, LocalDate policyApplicationDate) {
        this.roomId = roomId;
        this.roomOperationPolicyId = roomOperationPolicyId;
        this.policyApplicationDate = policyApplicationDate;
    }

    public RoomOperationPolicySchedule toEntity(Room room ,RoomOperationPolicy roomOperationPolicy) {
        return RoomOperationPolicySchedule.builder()
                .room(room)
                .roomOperationPolicy(roomOperationPolicy)
                .policyApplicationDate(policyApplicationDate)
                .build();
    }
}
