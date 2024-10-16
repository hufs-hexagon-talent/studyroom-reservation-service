package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.common.service.CommonHelperService;
import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.request.ModifyOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.mapper.RoomOperationPolicyMapper;
import hufs.computer.studyroom.domain.policy.repository.RoomOperationPolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyCommandService {
    private final RoomOperationPolicyRepository policyRepository;
    private final RoomOperationPolicyMapper policyMapper;
    private final CommonHelperService commonHelperService;

    public OperationPolicyInfoResponse createPolicy(CreateOperationPolicyRequest request) {
        RoomOperationPolicy roomOperationPolicy = policyMapper.toRoomOperationPolicy(request);
        RoomOperationPolicy savedPolicy = policyRepository.save(roomOperationPolicy);

        return policyMapper.toInfoResponse(savedPolicy);
    }

    public OperationPolicyInfoResponse updatePolicy(Long policyId, ModifyOperationPolicyRequest request) {
        RoomOperationPolicy policy = commonHelperService.getPolicyById(policyId);

        policyMapper.updatePolicy(request, policy);

        RoomOperationPolicy savedPolicy = policyRepository.save(policy);
        return policyMapper.toInfoResponse(savedPolicy);
    }

    public void deletePolicy(Long policyId) {
        commonHelperService.getPolicyById(policyId); // todo validator 로 이관
        policyRepository.deleteById(policyId);
    }
}
