package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.dto.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.RoomOperationPolicyUpdateDto;

import java.util.List;

public interface RoomOperationPolicyService {
    RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto);
    RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto);
    void deletePolicy(Long policyId);
    RoomOperationPolicy findPolicyById(Long policyId);
    List<RoomOperationPolicy> getAllPolicies();
}
