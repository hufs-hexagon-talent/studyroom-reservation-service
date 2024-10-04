package hufs.computer.studyroom.dto.operationpolicyschedule;

import hufs.computer.studyroom.domain.entity.Room;
import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.entity.RoomOperationPolicySchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
public class ScheduleResponseDto {
    private Long roomOperationPolicyScheduleId;
    private Long roomId;
    private Long roomOperationPolicyId;
    private LocalDate policyApplicationDate;


    public ScheduleResponseDto(Long roomOperationPolicyScheduleId, Long roomId, Long roomOperationPolicyId, LocalDate policyApplicationDate) {
        this.roomOperationPolicyScheduleId = roomOperationPolicyScheduleId;
        this.roomId = roomId;
        this.roomOperationPolicyId = roomOperationPolicyId;
        this.policyApplicationDate = policyApplicationDate;
    }

    public RoomOperationPolicySchedule toEntity(Room room , RoomOperationPolicy roomOperationPolicy) {
        return RoomOperationPolicySchedule.builder()
                .roomOperationPolicyScheduleId(roomOperationPolicyScheduleId)
                .room(room)
                .roomOperationPolicy(roomOperationPolicy)
                .policyApplicationDate(policyApplicationDate)
                .build();
    }
}
