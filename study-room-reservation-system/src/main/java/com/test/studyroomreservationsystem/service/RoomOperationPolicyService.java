package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.dto.operationpolicy.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.operationpolicy.RoomOperationPolicyUpdateDto;

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
