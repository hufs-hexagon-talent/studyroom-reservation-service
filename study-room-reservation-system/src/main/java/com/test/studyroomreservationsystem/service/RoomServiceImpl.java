package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import com.test.studyroomreservationsystem.dto.ReservationDto;
import com.test.studyroomreservationsystem.dto.RoomDto;
import com.test.studyroomreservationsystem.dto.RoomUpdateDto;
import com.test.studyroomreservationsystem.service.exception.RoomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;


@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomOperationPolicyService policyService; // 주입
    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository,
                           RoomOperationPolicyService policyService) {
        this.roomRepository = roomRepository;
        this.policyService = policyService;
    }



    @Override
    public Room createRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomName(roomDto.getRoomName());
        room.setRoomOperationPolicy(policyService.findPolicyById(roomDto.getRoomOperationPolicyId()));
        return roomRepository.save(room);
    }

    @Override
    public RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(room.getRoomId());
        roomDto.setRoomOperationPolicyId(room.getRoomOperationPolicy().getRoomOperationPolicyId());
        roomDto.setRoomName(room.getRoomName());
        return roomDto;
    }

    @Override
    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + roomId));
    }
    @Override
    public Room findRoomByName(String roomName) {
        return roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with name: " + roomName));
    }


    @Override
    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto) {
        Room room = findRoomById(roomId);
        room.setRoomName(roomUpdateDto.getRoomName());

        if (roomUpdateDto.getRoomOperationPolicyId() != null) {
            RoomOperationPolicy policy = policyService.findPolicyById(roomUpdateDto.getRoomOperationPolicyId());
            room.setRoomOperationPolicy(policy);
        }

        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override // 룸이 운영시간인지?
    public boolean isRoomAvailable(Long roomId, ReservationDto createDto) {
        Room room = findRoomById(roomId);

        RoomOperationPolicy roomOperationPolicy = room.getRoomOperationPolicy();
        LocalTime operationStartTime = roomOperationPolicy.getOperationStartTime();
        LocalTime operationEndTime = roomOperationPolicy.getOperationEndTime();

        LocalTime reservationStartTime = createDto.getStartDateTime().toLocalTime();
        LocalTime reservationEndTime = createDto.getEndDateTime().toLocalTime();


        return !operationStartTime.isAfter(reservationStartTime) && !operationEndTime.isBefore(reservationEndTime);
    }
}
