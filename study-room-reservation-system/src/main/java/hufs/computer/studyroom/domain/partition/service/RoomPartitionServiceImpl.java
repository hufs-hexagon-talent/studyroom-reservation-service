package hufs.computer.studyroom.domain.partition.service;

import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.error.exception.todo.reservation.RoomPolicyNotFoundException;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponses;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.mapper.RoomPartitionMapper;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.partition.dto.request.CreatePartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.request.ModifyPartitionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomPartitionServiceImpl implements RoomPartitionService {
    private final RoomPartitionRepository partitionRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomPartitionMapper partitionMapper;
    private final RoomRepository roomRepository;


    @Override
    public PartitionInfoResponse createRoomPartition(CreatePartitionRequest requestDto) {
        Room room = roomRepository.findById(requestDto.roomId()).orElseThrow(() -> new CustomException(RoomErrorCode.PARTITION_NOT_FOUND));
        RoomPartition partition = partitionMapper.toRoomPartition(requestDto, room);
        partitionRepository.save(partition);
        return partitionMapper.toInfoResponse(partition);
    }

    @Override
    public PartitionInfoResponse findRoomPartitionById(Long roomPartitionId) {
        RoomPartition roomPartition = partitionRepository.findById(roomPartitionId).orElseThrow(() -> new CustomException(RoomErrorCode.PARTITION_NOT_FOUND));
        return partitionMapper.toInfoResponse(roomPartition);
    }

    @Override
    public PartitionInfoResponses findByRoomId(Long roomId) {
        roomRepository.findById(roomId).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND)); // todo validatuon 애노테이션으로 컨트롤러단에서 처리
        List<RoomPartition> partitions = partitionRepository.findByRoom_RoomId(roomId);

        return partitionMapper.toPartitionInfoResponses(partitions);
    }

    @Override
    public PartitionInfoResponses findAll() {
        List<RoomPartition> partitions = partitionRepository.findAll();
        return partitionMapper.toPartitionInfoResponses(partitions);
    }


    @Override
    public PartitionInfoResponse modifyPartition(Long partitionId, ModifyPartitionRequest updateRequestDto) {
        // 업데이트 할 파티션이 존재하는지?
        RoomPartition roomPartition = partitionRepository.findById(partitionId).orElseThrow(() -> new CustomException(RoomErrorCode.PARTITION_NOT_FOUND));
        Room room = roomRepository.findById(updateRequestDto.roomId()).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        roomPartition.setRoom(room);
        roomPartition.setPartitionNumber(updateRequestDto.partitionNumber());
        RoomPartition updatedPartition = partitionRepository.save(roomPartition);
        return partitionMapper.toInfoResponse(updatedPartition);
    }

    @Override
    public Void deletePartitionById(Long partitionId) {
        partitionRepository.findById(partitionId).orElseThrow(() -> new CustomException(RoomErrorCode.PARTITION_NOT_FOUND)); // todo validatuon 애노테이션으로 컨트롤러단에서 처리
        partitionRepository.deleteById(partitionId);
        return null;
    }

    @Override
    public PartitionPolicyResponses getPartitionsPolicyByDate(LocalDate date) {
//        todo 수정
        List<RoomPartition> partitions = partitionRepository.findAll();
        List<PartitionPolicyResponse> responseList = new ArrayList<>();
        for (RoomPartition partition : partitions) {
            RoomOperationPolicy policy = null;
            Room room = partition.getRoom();
            try {
                RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date).orElseThrow(() -> new RoomPolicyNotFoundException(room, date)// 운영이 하지 않음 (운영 정책 없음)
                        );
                policy = schedule.getRoomOperationPolicy();
            }catch (RoomPolicyNotFoundException e) {
                // 정책이 없을 때는 policyId가 null로 유지됨
            }
            PartitionPolicyResponse response = partitionMapper.toPolicyResponse(partition, policy);
            responseList.add(response);
            }

        return partitionMapper.toPolicyResponses(responseList);
    }

}
