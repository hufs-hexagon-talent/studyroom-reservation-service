package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.domain.partition.dto.request.CreatePartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.request.ModifyPartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponses;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;

import java.time.LocalDate;
import java.util.List;

public interface RoomPartitionService {
    PartitionInfoResponse createRoomPartition(CreatePartitionRequest request );
    PartitionInfoResponse findRoomPartitionById(Long roomPartitionId);
    PartitionInfoResponse modifyPartition(Long partitionId, ModifyPartitionRequest updateRequestDto);
    PartitionInfoResponses findByRoomId(Long roomId);
    PartitionInfoResponses findAll();
    Void deletePartitionById(Long roomPartitionId);
    PartitionPolicyResponses getPartitionsPolicyByDate(LocalDate date);

}
