package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;
import com.test.studyroomreservationsystem.dto.room.RoomsResponseDto;
import com.test.studyroomreservationsystem.exception.reservation.OperationClosedException;
import com.test.studyroomreservationsystem.service.RoomService;
import com.test.studyroomreservationsystem.exception.notfound.RoomNotFoundException;
import com.test.studyroomreservationsystem.exception.reservation.RoomPolicyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        Room roomEntity = roomDto.toEntity();
        return roomRepository.save(roomEntity);
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
        Room roomEntity = findRoomById(roomId);
        roomEntity.setRoomName(roomUpdateDto.getRoomName());

        return roomRepository.save(roomEntity);
    }

    @Override
    public void deleteRoom(Long roomId) {
        findRoomById(roomId); // 찾아보고 없으면 예외처리
        roomRepository.deleteById(roomId);
    }

    @Override // 룸이 운영을 하는지? && 운영이 종료 되었는지?
    public void isRoomAvailable(Long roomId, Instant startDateTime, Instant endDateTime) {

        Room room = findRoomById(roomId);
        LocalDate date = startDateTime.atZone(ZoneOffset.UTC).toLocalDate();

        // 룸과 날짜로 정책 찾기
        RoomOperationPolicySchedule schedule
                = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                .orElseThrow(
                        // 운영이 하지 않음 (운영 정책 없음)
                        () -> new RoomPolicyNotFoundException(room, date)
                );


        RoomOperationPolicy roomOperationPolicy = schedule.getRoomOperationPolicy();

        LocalTime operationStartTime = roomOperationPolicy.getOperationStartTime();
        LocalTime operationEndTime = roomOperationPolicy.getOperationEndTime();

        LocalTime reservationStartTime = startDateTime.atZone(ZoneOffset.UTC).toLocalTime();
        LocalTime reservationEndTime = endDateTime.atZone(ZoneOffset.UTC).toLocalTime();

        if (operationStartTime.isAfter(reservationStartTime) && operationEndTime.isBefore(reservationEndTime)) {
            throw new OperationClosedException(room, operationStartTime, operationEndTime);
        }

    }

    // todo 수정해야해
    @Override
    public List<RoomsResponseDto> getRoomsPolicyByDate(LocalDate date) {
        List<Room> rooms = roomRepository.findAll();
        List<RoomsResponseDto> responseList = new ArrayList<>();

        for (Room room : rooms) {
            RoomOperationPolicy policy = null;
            try {
                RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                        .orElseThrow(() -> new RoomPolicyNotFoundException(room, date));
                policy = schedule.getRoomOperationPolicy();

            } catch (RoomPolicyNotFoundException e) {
                // 정책이 없을 때는 policyId가 null로 유지됨
            }
                responseList.add(new RoomsResponseDto(room.getRoomId(), room.getRoomName(), policy));
        }
        return responseList;
    }
}
