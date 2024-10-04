package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.repository.RoomOperationPolicyRepository;
import hufs.computer.studyroom.domain.policy.dto.RoomOperationPolicyDto;
import hufs.computer.studyroom.domain.policy.dto.RoomOperationPolicyUpdateDto;
import hufs.computer.studyroom.common.error.exception.notfound.PolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomOperationPolicyServiceImpl implements RoomOperationPolicyService {

    RoomOperationPolicyRepository policyRepository;
    @Autowired
    public RoomOperationPolicyServiceImpl(RoomOperationPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto) {
        RoomOperationPolicy policyEntity = policyDto.toEntity();
        return policyRepository.save(policyEntity);
    }


    @Override
    public RoomOperationPolicy findPolicyById(Long policyId) {
        return policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));
    }

    @Override
    public List<RoomOperationPolicy> findAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy policy = findPolicyById(policyId);
        if (policyDto.getOperationStartTime() != null) {
            policy.setOperationStartTime(policyDto.getOperationStartTime());
        }
        if (policyDto.getOperationEndTime() != null) {
            policy.setOperationEndTime(policyDto.getOperationEndTime());
        }
        if (policyDto.getEachMaxMinute() != null) {
            policy.setEachMaxMinute(policyDto.getEachMaxMinute());
        }

        return policyRepository.save(policy);
    }

    @Override
    public void deletePolicy(Long policyId) {
        findPolicyById(policyId); // 찾아보고 없으면 예외처리
        policyRepository.deleteById(policyId);
    }


}
