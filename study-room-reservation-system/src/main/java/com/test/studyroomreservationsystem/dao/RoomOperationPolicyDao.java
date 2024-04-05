package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;

import java.util.List;
import java.util.Optional;

public interface RoomOperationPolicyDao {
    RoomOperationPolicy save(RoomOperationPolicy policy);

    Optional<RoomOperationPolicy> findById(Long policyId);

    List<RoomOperationPolicy> findAll();

    void deleteById(Long policyId);
}
