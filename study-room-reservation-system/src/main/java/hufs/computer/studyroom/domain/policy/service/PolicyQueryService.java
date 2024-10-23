package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.common.error.code.PolicyErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponses;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.mapper.RoomOperationPolicyMapper;
import hufs.computer.studyroom.domain.policy.repository.RoomOperationPolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyQueryService {
    private final RoomOperationPolicyRepository policyRepository;
    private final RoomOperationPolicyMapper policyMapper;

    public OperationPolicyInfoResponse findPolicyById(Long policyId) {
        RoomOperationPolicy policy = getPolicyById(policyId);
        return policyMapper.toInfoResponse(policy);
    }

    public OperationPolicyInfoResponses findAllPolicies() {
        List<RoomOperationPolicy> policies = policyRepository.findAll();
        return policyMapper.toOperationPolicyInfoResponses(policies);
    }

    public RoomOperationPolicy getPolicyById(Long id) {
        return policyRepository.findById(id).orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND));
    }

    public boolean existPolicyById(Long policyId) {
        return policyRepository.existsById(policyId);
    }
}
