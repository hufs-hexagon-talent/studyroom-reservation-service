package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.domain.partition.dto.request.CreatePartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.request.ModifyPartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.mapper.RoomPartitionMapper;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.service.RoomQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PartitionCommandService {
    private final RoomPartitionRepository partitionRepository;
    private final RoomPartitionMapper partitionMapper;
    private final PartitionQueryService partitionQueryService;
    private final RoomQueryService roomQueryService;

    public PartitionInfoResponse createRoomPartition(CreatePartitionRequest request) {
        Room room = roomQueryService.getRoomById(request.roomId());
        RoomPartition partition = partitionMapper.toRoomPartition(request, room);
        partitionRepository.save(partition);
        return partitionMapper.toInfoResponse(partition);
    }

    public PartitionInfoResponse modifyPartition(Long partitionId, ModifyPartitionRequest request) {

        RoomPartition partition = partitionQueryService.getPartitionById(partitionId);
        Room room = roomQueryService.getRoomById(request.roomId());

        partitionMapper.updateFromRequest(request, room, partition);

        RoomPartition updatedPartition = partitionRepository.save(partition);
        return partitionMapper.toInfoResponse(updatedPartition);
    }

    public void deletePartitionById(Long partitionId) {
        partitionRepository.deleteById(partitionId);
    }
}
