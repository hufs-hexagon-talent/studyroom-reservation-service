package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyRepository;
import com.test.studyroomreservationsystem.dto.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.RoomOperationPolicyUpdateDto;
import com.test.studyroomreservationsystem.service.exception.RoomOperationPolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomOperationPolicyServiceImpl implements RoomOperationPolicyService{

    RoomOperationPolicyRepository roomOperationPolicyRepository;
    @Autowired
    public RoomOperationPolicyServiceImpl(RoomOperationPolicyRepository roomOperationPolicyRepository) {
        this.roomOperationPolicyRepository = roomOperationPolicyRepository;
    }

    @Override
    public RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto) {
        RoomOperationPolicy roomOperationPolicy = new RoomOperationPolicy();

        roomOperationPolicy.setOperationStartTime(policyDto.getOperationStartTime());
        roomOperationPolicy.setOperationEndTime(policyDto.getOperationEndTime());
        roomOperationPolicy.setEachMaxMinute(policyDto.getEachMaxMinute());

        return roomOperationPolicyRepository.save(roomOperationPolicy);
    }
    @Override
    public RoomOperationPolicy findPolicyById(Long policyId) {
        return roomOperationPolicyRepository.findById(policyId)
                .orElseThrow(()-> new RoomOperationPolicyNotFoundException("RoomOperationPolicy not found with id: " + policyId));
    }

    @Override
    public List<RoomOperationPolicy> getAllPolicies() {
        return roomOperationPolicyRepository.findAll();
    }

    @Override
    public RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy roomOperationPolicy = findPolicyById(policyId);
        roomOperationPolicy.setOperationStartTime(policyDto.getOperationStartTime());
        roomOperationPolicy.setOperationEndTime(policyDto.getOperationEndTime());
        roomOperationPolicy.setEachMaxMinute(policyDto.getEachMaxMinute());
        return roomOperationPolicy;
    }

    @Override
    public void deletePolicy(Long policyId) {
        roomOperationPolicyRepository.deleteById(policyId);
    }


}
