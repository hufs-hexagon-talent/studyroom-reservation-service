package hufs.computer.studyroom.domain.policy.mapper;

import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponses;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomOperationPolicyMapper {

    // CreateOperationPolicyRequest -> RoomOperationPolicy 변환
    RoomOperationPolicy toRoomOperationPolicy(CreateOperationPolicyRequest request);

    // RoomOperationPolicy -> OperationPolicyInfoResponse 변환
    OperationPolicyInfoResponse toInfoResponse(RoomOperationPolicy operationPolicy);

    default OperationPolicyInfoResponses toOperationPolicyInfoResponses(List<RoomOperationPolicy> policies) {
        List<OperationPolicyInfoResponse> responses = policies.stream()
                .map(this::toInfoResponse)
                .toList();

        return OperationPolicyInfoResponses.builder()
                .operationPolicyInfos(responses)
                .build();
    }

}
