package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyUpdateDto;

import java.util.List;

public interface RoomOperationPolicyService {
    RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto);
    RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto);
    void deletePolicy(Long policyId);
    RoomOperationPolicy findPolicyById(Long policyId);
    List<RoomOperationPolicy> findAllPolicies();

    default RoomOperationPolicyDto dtoFrom(RoomOperationPolicy roomOperationPolicy) {
        return RoomOperationPolicyDto.builder()
                .operationStartTime(roomOperationPolicy.getOperationStartTime())
                .operationEndTime(roomOperationPolicy.getOperationEndTime())
                .eachMaxMinute(roomOperationPolicy.getEachMaxMinute())
                .build();
    }

}
