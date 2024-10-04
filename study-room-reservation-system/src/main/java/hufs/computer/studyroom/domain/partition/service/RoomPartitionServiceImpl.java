package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.common.error.exception.notfound.PartitionNotFoundException;
import hufs.computer.studyroom.common.error.exception.reservation.RoomPolicyNotFoundException;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.service.RoomService;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.room.service.RoomService;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.partition.dto.PartitionPolicyResponseDto;
import hufs.computer.studyroom.domain.partition.dto.PartitionRequestDto;
import hufs.computer.studyroom.domain.partition.dto.PartitionUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomPartitionServiceImpl implements RoomPartitionService {
    private final RoomPartitionRepository partitionRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomService roomService;

    public RoomPartitionServiceImpl(RoomPartitionRepository partitionRepository, RoomOperationPolicyScheduleRepository scheduleRepository, RoomService roomService) {
        this.partitionRepository = partitionRepository;
        this.scheduleRepository = scheduleRepository;
        this.roomService = roomService;
    }


    @Override
    public RoomPartition createRoomPartition(PartitionRequestDto requestDto) {
        Long roomId = requestDto.getRoomId();
        RoomPartition partitionEntity = requestDto.toEntity(roomService.findRoomById(roomId));
        return partitionRepository.save(partitionEntity);
    }

    @Override
    public RoomPartition findRoomPartitionById(Long roomPartitionId) {
        return partitionRepository.findById(roomPartitionId).orElseThrow(()-> new PartitionNotFoundException(roomPartitionId));
    }

    @Override
    public List<RoomPartition> findRoomPartitionsByRoomId(Long roomId) {
        roomService.findRoomById(roomId);
        return partitionRepository.findByRoom_RoomId(roomId);
    }

    @Override
    public List<RoomPartition> findAllRoomPartition() {
        return partitionRepository.findAll();
    }

    @Override
    public List<RoomPartition> findRoomPartitionByRoomId(Long roomId) {
        return partitionRepository.findByRoom_RoomId(roomId);
    }

    @Override
    public RoomPartition updateRoomPartition(Long partitionId, PartitionUpdateRequestDto updateRequestDto) {
        // 업데이트 할 파티션이 존재하는지?
        findRoomPartitionById(partitionId);

        return null;
    }

    @Override
    public void deletePartitionById(Long roomPartitionId) {
        findRoomPartitionById(roomPartitionId);
        partitionRepository.deleteById(roomPartitionId);
    }
    @Override
    public List<PartitionPolicyResponseDto> getPartitionsPolicyByDate(LocalDate date) {
        List<RoomPartition> partitions = findAllRoomPartition();
        List<PartitionPolicyResponseDto> responseList = new ArrayList<>();

        for (RoomPartition partition : partitions) {
            RoomOperationPolicy policy = null;
            Room room = partition.getRoom();
            try {
                RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                        .orElseThrow(
                                // 운영이 하지 않음 (운영 정책 없음)
                                () -> new RoomPolicyNotFoundException(room, date)
                        );
                policy = schedule.getRoomOperationPolicy();
            }catch (RoomPolicyNotFoundException e) {
                // 정책이 없을 때는 policyId가 null로 유지됨
            }
            responseList.add(new PartitionPolicyResponseDto(
                    partition.getRoomPartitionId(),
                    room.getRoomId(),
                    room.getRoomName(),
                    partition.getPartitionNumber(),
                    policy
                    ));
        }

        return responseList;
    }

}
