package hufs.computer.studyroom.domain.partition.mapper;

import hufs.computer.studyroom.domain.partition.dto.request.CreatePartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.request.ModifyPartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponses;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.mapper.RoomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomMapper.class}) // MapStruct에서 생성된 Mapper 구현체를 Spring Bean으로 등록
public interface RoomPartitionMapper {
    // request 변환 (RequestDTO -> Entity)
    @Mapping(target = "room", source = "room")
    @Mapping(target = "roomPartitionId", ignore = true)
    RoomPartition toRoomPartition(CreatePartitionRequest request , Room room);

    @Mapping(target = "roomPartitionId", ignore = true)
    @Mapping(target = "room", source = "room")
    void updateFromRequest(ModifyPartitionRequest request, Room room , @MappingTarget RoomPartition partition);

    // RoomPartition -> PartitionInfoResponse 변환
    @Mapping(source = "room.roomId", target = "roomId")
    @Mapping(source = "room.roomName", target = "roomName")
    PartitionInfoResponse toInfoResponse(RoomPartition partition);

    // Partition 변환 (Entity -> ResponseDTO)
    @Mapping(source = "partition", target = "partitionInfo")
    @Mapping(source = "policy", target = "policy")
    PartitionPolicyResponse toPolicyResponse(RoomPartition partition, RoomOperationPolicy policy);

    // 리스트를 PartitionInfoResponses로 감싸는 변환
    default PartitionInfoResponses toPartitionInfoResponses(List<RoomPartition> partitions) {
        List<PartitionInfoResponse> responses = partitions.stream()
                .map(this::toInfoResponse)
                .toList();
        return PartitionInfoResponses.builder()
                .partitions(responses)
                .build();
    }

    // 리스트를 PartitionPolicyResponses로 감싸는 변환
    default PartitionPolicyResponses toPolicyResponses(List<PartitionPolicyResponse> partitionPolicies) {
        return PartitionPolicyResponses.builder()
                .partitionPolicies((partitionPolicies))
                .build();
    }
}
