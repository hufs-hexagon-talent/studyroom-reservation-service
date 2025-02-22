package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponses;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.mapper.RoomPartitionMapper;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.service.ScheduleQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartitionQueryService {
    private final RoomPartitionRepository partitionRepository;
    private final ScheduleQueryService scheduleQueryService;
    private final RoomPartitionMapper partitionMapper;

    public PartitionInfoResponse findRoomPartitionById(Long roomPartitionId) {
        RoomPartition partition = getPartitionById(roomPartitionId);
        return partitionMapper.toInfoResponse(partition);
    }

    public PartitionInfoResponses findAll() {
        List<RoomPartition> partitions = partitionRepository.findAll();
        return partitionMapper.toPartitionInfoResponses(partitions);
    }

    public PartitionPolicyResponses getPartitionsPolicyByDate(LocalDate date) {
//        todo 수정
        List<RoomPartition> partitions = partitionRepository.findAll();

        List<PartitionPolicyResponse> responseList = partitions.stream()
                .map(partition -> {
                    Room room = partition.getRoom();
                    RoomOperationPolicy policy = scheduleQueryService.getScheduleByRoomAndDate(room, date)
                            .map(RoomOperationPolicySchedule::getRoomOperationPolicy)
                            .orElse(null);
                    return partitionMapper.toPolicyResponse(partition, policy); // Mapper로 변환
                })
                .toList();
        return partitionMapper.toPolicyResponses(responseList);
    }

//        List<PartitionPolicyResponse> responseList = new ArrayList<>();
//        for (RoomPartition partition : partitions) {
//            RoomOperationPolicy policy = null;
//            Room room = partition.getRoom();
//            try {
//                RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date).orElseThrow(() -> new RoomPolicyNotFoundException(room, date)// 운영이 하지 않음 (운영 정책 없음)
//                );
//                policy = schedule.getRoomOperationPolicy();
//            }catch (RoomPolicyNotFoundException e) {
//                // 정책이 없을 때는 policyId가 null로 유지됨
//            }
//            PartitionPolicyResponse response = partitionMapper.toPolicyResponse(partition, policy);
//            responseList.add(response);
//        }

    public RoomPartition getPartitionById(Long id) {
        return partitionRepository.findById(id).orElseThrow(() -> new CustomException(RoomErrorCode.PARTITION_NOT_FOUND));
    }

    public boolean existByPartitionId(Long id) {
        return partitionRepository.existsById(id);
    }
}
