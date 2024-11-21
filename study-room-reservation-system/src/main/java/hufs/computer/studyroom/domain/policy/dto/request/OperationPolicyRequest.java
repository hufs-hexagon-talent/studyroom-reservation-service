package hufs.computer.studyroom.domain.policy.dto.request;

import java.time.LocalTime;

public interface OperationPolicyRequest {
    LocalTime operationStartTime();
    LocalTime operationEndTime();
}