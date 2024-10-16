package hufs.computer.studyroom.domain.policy.mapper;

import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.request.ModifyOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponses;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomOperationPolicyMapper {

    // CreateOperationPolicyRequest -> RoomOperationPolicy 변환
    @Mapping(target = "roomOperationPolicyId", ignore = true)
    RoomOperationPolicy toRoomOperationPolicy(CreateOperationPolicyRequest request);

    // ModifyOperationPolicyRequest -> RoomOperationPolicy 업데이트 (기존 엔티티 수정)
    void updatePolicy(ModifyOperationPolicyRequest request, @MappingTarget RoomOperationPolicy policy);

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
