package hufs.computer.studyroom.domain.policy.dto.request;

import lombok.Getter;

import java.time.LocalTime;
@Getter
public class ModifyOperationPolicyRequest {
    private LocalTime operationStartTime;
    private LocalTime operationEndTime;
    private Integer eachMaxMinute;
}
