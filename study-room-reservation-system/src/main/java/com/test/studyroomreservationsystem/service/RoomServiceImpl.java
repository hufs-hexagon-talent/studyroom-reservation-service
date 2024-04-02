package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;
import com.test.studyroomreservationsystem.service.exception.RoomNotFoundException;
import com.test.studyroomreservationsystem.service.exception.RoomPolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    @Autowired

    public RoomServiceImpl(RoomRepository roomRepository, RoomOperationPolicyScheduleRepository scheduleRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }



    @Override
    public Room createRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomName(roomDto.getRoomName());
        return roomRepository.save(room);
    }

    @Override
    public RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(room.getRoomId());
        roomDto.setRoomName(room.getRoomName());
        return roomDto;
    }

    @Override
    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
    }
    @Override
    public Room findRoomByName(String roomName) {
        return roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RoomNotFoundException(roomName));
    }


    @Override
    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto) {
        Room room = findRoomById(roomId);
        room.setRoomName(roomUpdateDto.getRoomName());

        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override // 룸이 운영시간인지?
    public boolean isRoomAvailable(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        Room room = findRoomById(roomId);
        LocalDate date = startDateTime.toLocalDate();

        // 룸과 날짜로 정책 찾기
        RoomOperationPolicySchedule schedule
                = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                .orElseThrow(
                        () -> new RoomPolicyNotFoundException(roomId, date)
                );


        RoomOperationPolicy roomOperationPolicy = schedule.getRoomOperationPolicy();

        LocalTime operationStartTime = roomOperationPolicy.getOperationStartTime();
        LocalTime operationEndTime = roomOperationPolicy.getOperationEndTime();

        LocalTime reservationStartTime = startDateTime.toLocalTime();
        LocalTime reservationEndTime = endDateTime.toLocalTime();


        return !operationStartTime.isAfter(reservationStartTime) && !operationEndTime.isBefore(reservationEndTime);
    }
}
