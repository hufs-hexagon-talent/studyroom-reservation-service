package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponses;
import hufs.computer.studyroom.domain.policy.dto.request.ModifyOperationPolicyRequest;
import hufs.computer.studyroom.domain.room.entity.Room;

import java.time.Instant;

public interface RoomOperationPolicyService {
    OperationPolicyInfoResponse createPolicy(CreateOperationPolicyRequest request);
    OperationPolicyInfoResponse updatePolicy(Long policyId, ModifyOperationPolicyRequest policyDto);
    void deletePolicy(Long policyId);
    OperationPolicyInfoResponse findPolicyById(Long policyId);
    OperationPolicyInfoResponses findAllPolicies();
    void validateRoomOperation(Room room, Instant reservationStartTime, Instant reservationEndTime);


}
