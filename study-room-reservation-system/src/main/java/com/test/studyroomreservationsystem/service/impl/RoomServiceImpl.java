package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.RoomDao;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;
import com.test.studyroomreservationsystem.service.RoomService;
import com.test.studyroomreservationsystem.service.exception.RoomNotFoundException;
import com.test.studyroomreservationsystem.service.exception.RoomPolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
public class RoomServiceImpl implements RoomService {
    private final RoomDao roomDao;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    @Autowired

    public RoomServiceImpl(RoomDao roomDao, RoomOperationPolicyScheduleRepository scheduleRepository) {
        this.roomDao = roomDao;
        this.scheduleRepository = scheduleRepository;
    }



    @Override
    public Room createRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomName(roomDto.getRoomName());
        return roomDao.save(room);
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
        return roomDao.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
    }
    @Override
    public Room findRoomByName(String roomName) {
        return roomDao.findByRoomName(roomName)
                .orElseThrow(() -> new RoomNotFoundException(roomName));
    }


    @Override
    public List<Room> findAllRoom() {
        return roomDao.findAll();
    }

    @Override
    public Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto) {
        Room room = findRoomById(roomId);
        room.setRoomName(roomUpdateDto.getRoomName());

        return roomDao.save(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomDao.deleteById(roomId);
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