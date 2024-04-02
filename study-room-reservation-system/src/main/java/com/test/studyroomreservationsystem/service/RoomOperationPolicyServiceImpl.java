package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyRepository;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyUpdateDto;
import com.test.studyroomreservationsystem.service.exception.RoomOperationPolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomOperationPolicyServiceImpl implements RoomOperationPolicyService{

    RoomOperationPolicyRepository policyRepository;
    @Autowired
    public RoomOperationPolicyServiceImpl(RoomOperationPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto) {
        RoomOperationPolicy policy = new RoomOperationPolicy();

        policy.setOperationStartTime(policyDto.getOperationStartTime());
        policy.setOperationEndTime(policyDto.getOperationEndTime());
        policy.setEachMaxMinute(policyDto.getEachMaxMinute());

        return policyRepository.save(policy);
    }
    @Override
    public RoomOperationPolicyDto convertToDto(RoomOperationPolicy policy) {

        RoomOperationPolicyDto policyDto = new RoomOperationPolicyDto();
        policyDto.setOperationStartTime(policy.getOperationStartTime());
        policyDto.setOperationEndTime(policy.getOperationEndTime());
        policyDto.setEachMaxMinute(policy.getEachMaxMinute());

        return policyDto;
    }
    @Override
    public RoomOperationPolicyUpdateDto convertToUpdateDto(RoomOperationPolicy policy) {

        RoomOperationPolicyUpdateDto policyDto = new RoomOperationPolicyUpdateDto();
        policyDto.setOperationStartTime(policy.getOperationStartTime());
        policyDto.setOperationEndTime(policy.getOperationEndTime());
        policyDto.setEachMaxMinute(policy.getEachMaxMinute());

        return policyDto;
    }

    @Override
    public RoomOperationPolicy findPolicyById(Long policyId) {
        return policyRepository.findById(policyId)
                .orElseThrow(() -> new RoomOperationPolicyNotFoundException("RoomOperationPolicy not found with id: " + policyId));
    }

    @Override
    public List<RoomOperationPolicy> findAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy policy = findPolicyById(policyId);

        policy.setOperationStartTime(policyDto.getOperationStartTime());
        policy.setOperationEndTime(policyDto.getOperationEndTime());
        policy.setEachMaxMinute(policyDto.getEachMaxMinute());

        return policyRepository.save(policy);
    }

    @Override
    public void deletePolicy(Long policyId) {
        policyRepository.deleteById(policyId);
    }


}
