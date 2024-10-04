package hufs.computer.studyroom.dto.operationpolicy;

import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class RoomOperationPolicyDto {
    private final Long policyId;
    private final LocalTime operationStartTime;
    private final LocalTime operationEndTime;
    private final Integer eachMaxMinute;

    public RoomOperationPolicyDto(Long policyId, LocalTime operationStartTime, LocalTime operationEndTime, Integer eachMaxMinute) {
        this.policyId = policyId;
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;
        this.eachMaxMinute = eachMaxMinute;
    }

    public RoomOperationPolicy toEntity() {
        return RoomOperationPolicy.builder()
                .roomOperationPolicyId(policyId)
                .operationStartTime(operationStartTime)
                .operationEndTime(operationEndTime)
                .eachMaxMinute(eachMaxMinute)
                .build();
    }
}
