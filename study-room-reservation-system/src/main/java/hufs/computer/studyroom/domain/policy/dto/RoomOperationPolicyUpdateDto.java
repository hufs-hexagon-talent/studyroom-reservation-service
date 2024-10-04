package hufs.computer.studyroom.domain.policy.dto;

import lombok.Getter;

import java.time.LocalTime;
@Getter
public class RoomOperationPolicyUpdateDto {
    private LocalTime operationStartTime;
    private LocalTime operationEndTime;
    private Integer eachMaxMinute;
}
