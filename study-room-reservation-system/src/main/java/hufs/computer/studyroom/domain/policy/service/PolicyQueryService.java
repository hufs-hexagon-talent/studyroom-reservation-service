package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.common.service.CommonHelperService;
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
    private final CommonHelperService commonHelperService;

    public OperationPolicyInfoResponse findPolicyById(Long policyId) {
        RoomOperationPolicy policy = commonHelperService.getPolicyById(policyId);
        return policyMapper.toInfoResponse(policy);
    }

    public OperationPolicyInfoResponses findAllPolicies() {
        List<RoomOperationPolicy> policies = policyRepository.findAll();
        return policyMapper.toOperationPolicyInfoResponses(policies);
    }
}
