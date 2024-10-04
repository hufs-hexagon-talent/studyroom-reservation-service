package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.dto.RoomOperationPolicyDto;
import hufs.computer.studyroom.domain.policy.dto.RoomOperationPolicyUpdateDto;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;

import java.util.List;

public interface RoomOperationPolicyService {
    RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto);
    RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto);
    void deletePolicy(Long policyId);
    RoomOperationPolicy findPolicyById(Long policyId);
    List<RoomOperationPolicy> findAllPolicies();

    default RoomOperationPolicyDto dtoFrom(RoomOperationPolicy roomOperationPolicy) {
        return RoomOperationPolicyDto.builder()
                .policyId(roomOperationPolicy.getRoomOperationPolicyId())
                .operationStartTime(roomOperationPolicy.getOperationStartTime())
                .operationEndTime(roomOperationPolicy.getOperationEndTime())
                .eachMaxMinute(roomOperationPolicy.getEachMaxMinute())
                .build();
    }

}
