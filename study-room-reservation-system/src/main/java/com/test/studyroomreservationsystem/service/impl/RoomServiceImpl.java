package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.dao.RoomDao;
import com.test.studyroomreservationsystem.dao.RoomOperationPolicyScheduleDao;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomsReservationResponseDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;
import com.test.studyroomreservationsystem.service.RoomService;
import com.test.studyroomreservationsystem.service.exception.RoomNotFoundException;
import com.test.studyroomreservationsystem.service.exception.RoomPolicyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class RoomServiceImpl implements RoomService {
    private final RoomDao roomDao;
    private final ReservationDao reservationDao;
    private final RoomOperationPolicyScheduleDao scheduleDao;
    @Autowired

    public RoomServiceImpl(RoomDao roomDao, ReservationDao reservationDao, RoomOperationPolicyScheduleDao scheduleDao) {
        this.roomDao = roomDao;
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Room createRoom(RoomDto roomDto) {
        Room roomEntity = roomDto.toEntity();
        return roomDao.save(roomEntity);
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
        Room roomEntity = findRoomById(roomId);
        roomEntity.setRoomName(roomUpdateDto.getRoomName());

        return roomDao.save(roomEntity);
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
                = scheduleDao.findByRoomAndPolicyApplicationDate(room, date)
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

    @Override
    public List<RoomsReservationResponseDto> getRoomReservationsByDate(LocalDate date) {
        List<Room> rooms = roomDao.findAll();
        ArrayList<RoomsReservationResponseDto> responseList = new ArrayList<>();

        for (Room room : rooms) {
            // todo : 해당 날짜의 운영시간이 모두 같다면 위로 올리자
            RoomOperationPolicySchedule schedule = scheduleDao.findByRoomAndPolicyApplicationDate(room, date)
                    .orElseThrow(
                            () -> new RoomPolicyNotFoundException(room.getRoomId(), date)
                    );

            LocalTime operationStartTime = schedule.getRoomOperationPolicy().getOperationStartTime();
            LocalTime operationEndTime = schedule.getRoomOperationPolicy().getOperationEndTime();
            LocalDateTime operationStartDateTime = date.atTime(operationStartTime);
            LocalDateTime operationEndDateTime = date.atTime(operationEndTime);

            // 각 룸의 예약들
            List<Reservation> reservations = reservationDao.findOverlappingReservations(room.getRoomId(), operationStartDateTime, operationEndDateTime);
            List<RoomsReservationResponseDto.TimeRange> reservationTimes
                    = reservations.stream()
                    .map(reservation -> new RoomsReservationResponseDto.TimeRange(reservation.getReservationStartTime(), reservation.getReservationEndTime()))
                    .toList();
            responseList.add(new RoomsReservationResponseDto(room.getRoomId(), reservationTimes));
        }
        return responseList;
    }
}
