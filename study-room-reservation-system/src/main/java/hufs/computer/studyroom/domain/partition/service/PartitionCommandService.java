package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.common.service.CommonHelperService;
import hufs.computer.studyroom.domain.partition.dto.request.CreatePartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.request.ModifyPartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.mapper.RoomPartitionMapper;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartitionCommandService {
    private final RoomPartitionRepository partitionRepository;
    private final RoomPartitionMapper partitionMapper;
    private final CommonHelperService commonHelperService;

    public PartitionInfoResponse createRoomPartition(CreatePartitionRequest request) {
        Room room = commonHelperService.getRoomById(request.roomId());
        RoomPartition partition = partitionMapper.toRoomPartition(request, room);
        partitionRepository.save(partition);
        return partitionMapper.toInfoResponse(partition);
    }

    public PartitionInfoResponse modifyPartition(Long partitionId, ModifyPartitionRequest request) {
        // 업데이트 할 파티션이 존재하는지?
        RoomPartition partition = commonHelperService.getPartitionById(partitionId);
        if (request.partitionNumber() != null) {
            partition.setPartitionNumber(request.partitionNumber());
        }
        if (request.roomId() != null) {
            Room room = commonHelperService.getRoomById(request.roomId());
            partition.setRoom(room);
        }

        RoomPartition updatedPartition = partitionRepository.save(partition);
        return partitionMapper.toInfoResponse(updatedPartition);
    }

    public Void deletePartitionById(Long partitionId) {
        commonHelperService.getPartitionById(partitionId);// todo validatuon 애노테이션으로 컨트롤러단에서 처리
        partitionRepository.deleteById(partitionId);
        return null;
    }
}
