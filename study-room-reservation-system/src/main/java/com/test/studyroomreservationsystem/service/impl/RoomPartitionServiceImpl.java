package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.domain.repository.RoomPartitionRepository;
import com.test.studyroomreservationsystem.dto.partition.PartitionRequestDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionResponseDto;
import com.test.studyroomreservationsystem.exception.notfound.PartitionNotFoundException;
import com.test.studyroomreservationsystem.service.RoomPartitionService;
import com.test.studyroomreservationsystem.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomPartitionServiceImpl implements RoomPartitionService {
    private final RoomService roomService;
    private final RoomPartitionRepository partitionRepository;

    public RoomPartitionServiceImpl(RoomService roomService, RoomPartitionRepository partitionRepository) {
        this.roomService = roomService;
        this.partitionRepository = partitionRepository;
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
    public void deletePartitionById(Long roomPartitionId) {
        findRoomPartitionById(roomPartitionId);
        partitionRepository.deleteById(roomPartitionId);
    }

}
