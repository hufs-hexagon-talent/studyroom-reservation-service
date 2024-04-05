package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.RoomOperationPolicyDao;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyUpdateDto;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyService;
import com.test.studyroomreservationsystem.service.exception.RoomOperationPolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomOperationPolicyServiceImpl implements RoomOperationPolicyService {

    RoomOperationPolicyDao policyDao;
    @Autowired
    public RoomOperationPolicyServiceImpl(RoomOperationPolicyDao policyDao) {
        this.policyDao = policyDao;
    }

    @Override
    public RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto) {
        RoomOperationPolicy policy = new RoomOperationPolicy();

        policy.setOperationStartTime(policyDto.getOperationStartTime());
        policy.setOperationEndTime(policyDto.getOperationEndTime());
        policy.setEachMaxMinute(policyDto.getEachMaxMinute());

        return policyDao.save(policy);
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
        return policyDao.findById(policyId)
                .orElseThrow(() -> new RoomOperationPolicyNotFoundException(policyId));
    }

    @Override
    public List<RoomOperationPolicy> findAllPolicies() {
        return policyDao.findAll();
    }

    @Override
    public RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy policy = findPolicyById(policyId);

        policy.setOperationStartTime(policyDto.getOperationStartTime());
        policy.setOperationEndTime(policyDto.getOperationEndTime());
        policy.setEachMaxMinute(policyDto.getEachMaxMinute());

        return policyDao.save(policy);
    }

    @Override
    public void deletePolicy(Long policyId) {
        policyDao.deleteById(policyId);
    }


}
