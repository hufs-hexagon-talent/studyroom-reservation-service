package com.test.studyroomreservationsystem.dao.impl;

import com.test.studyroomreservationsystem.dao.RoomOperationPolicyDao;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomOperationPolicyDaoImpl implements RoomOperationPolicyDao {
    private final RoomOperationPolicyRepository policyRepository;
    @Autowired
    public RoomOperationPolicyDaoImpl(RoomOperationPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public RoomOperationPolicy save(RoomOperationPolicy policy) {
        return policyRepository.save(policy);
    }

    @Override
    public Optional<RoomOperationPolicy> findById(Long policyId) {
        return policyRepository.findById(policyId);
    }
    @Override
    public List<RoomOperationPolicy> findAll() {
        return policyRepository.findAll();
    }

    @Override
    public void deleteById(Long policyId) {
        policyRepository.deleteById(policyId);
    }
}
