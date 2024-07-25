package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.domain.repository.RoomPartitionRepository;
import com.test.studyroomreservationsystem.dto.partition.PartitionPolicyResponseDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionRequestDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionResponseDto;
import com.test.studyroomreservationsystem.dto.room.RoomResponseDto;
import com.test.studyroomreservationsystem.exception.notfound.PartitionNotFoundException;
import com.test.studyroomreservationsystem.exception.reservation.RoomPolicyNotFoundException;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyScheduleService;
import com.test.studyroomreservationsystem.service.RoomPartitionService;
import com.test.studyroomreservationsystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<RoomPartition> findAllRoomPartition() {
        return partitionRepository.findAll();
    }

    @Override
    public List<RoomPartition> findRoomPartitionByRoomId(Long roomId) {
        return partitionRepository.findByRoom_RoomId(roomId);
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
