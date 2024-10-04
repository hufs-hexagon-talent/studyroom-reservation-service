package hufs.computer.studyroom.service;

import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import hufs.computer.studyroom.dto.operationpolicy.RoomOperationPolicyDto;
import hufs.computer.studyroom.dto.operationpolicy.RoomOperationPolicyUpdateDto;

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
