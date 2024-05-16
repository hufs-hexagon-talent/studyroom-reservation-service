package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRequestDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationStateDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;
import com.test.studyroomreservationsystem.service.ReservationService;
import com.test.studyroomreservationsystem.service.RoomService;
import com.test.studyroomreservationsystem.service.UserService;
import com.test.studyroomreservationsystem.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final RoomService roomService;
    private final UserService userService;
    @Autowired
    public ReservationServiceImpl(ReservationDao reservationDao, RoomService roomService, UserService userService) {
        this.reservationDao = reservationDao;
        this.roomService = roomService;
        this.userService = userService;
    }

    
    @Override
    public Reservation createReservation(ReservationRequestDto reservationRequestDto, User user) {
        Long roomId = reservationRequestDto.getRoomId();
        LocalDateTime startDateTime = reservationRequestDto.getStartDateTime();
        LocalDateTime endDateTime = reservationRequestDto.getEndDateTime();

        // 검증
        validateRoomAvailability(roomId, startDateTime, endDateTime);

        Reservation reservationEntity = reservationRequestDto.toEntity(
                user,
                roomService.findRoomById(roomId)
        );

        return reservationDao.save(reservationEntity);
        
        }


    @Override
    public Reservation findReservationById(Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(()->new ReservationNotFoundException(reservationId));
    }
    @Override
    public Reservation findRecentReservationByUserId(Long userId) {
        return reservationDao.findRecentByUserId(userId)
                .orElseThrow(() -> new ReservationNotFoundException("Recent reservation not found for user " + userId));
    }

    @Override
    public List<Reservation> findAllReservation() {
        return reservationDao.findAll();
    }

    // 해당 유저의 모든 reservation 로그 찾기
    @Override
    public List<Reservation> findAllReservationByUser(Long userId) {
        User user = userService.findUserById(userId);
        return reservationDao.findAllByUser(user).orElseThrow(
                () -> new ReservationHistoryNotFoundException(userId));
    }


    @Override
    public Reservation updateReservation(Long reservationId, ReservationRequestDto reservationRequestDto) {

        // todo : 수정 요망
        Long roomId = reservationRequestDto.getRoomId();
        LocalDateTime startDateTime = reservationRequestDto.getStartDateTime();
        LocalDateTime endDateTime = reservationRequestDto.getEndDateTime();
        // 예약 가능 여부 확인 로직

        validateRoomAvailability(roomId,startDateTime,endDateTime);
        // todo : 더이상 dto 에 userId 없으니 변경해야함
        Reservation reservation = findReservationById(reservationId);

        Room room = roomService.findRoomById(reservationRequestDto.getRoomId());
        reservation.setRoom(room);

        reservation.setReservationStartTime(reservationRequestDto.getStartDateTime());
        reservation.setReservationEndTime(reservationRequestDto.getEndDateTime());

        return reservationDao.save(reservation);
    }

    // 현재 예약 에서 시간만 변경
    @Override
    public Reservation updateTimeReservation(Long reservationId, ReservationTimeDto timeDto) {
        Reservation reservation = findReservationById(reservationId);

        Long roomId = reservation.getRoom().getRoomId();
        LocalDateTime startDateTime = timeDto.getStartDateTime();
        LocalDateTime endDateTime = timeDto.getEndDateTime();

        // 검증
        validateRoomAvailability(roomId,startDateTime,endDateTime);

        reservation.setReservationStartTime(startDateTime);
        reservation.setReservationEndTime(endDateTime);
        return reservationDao.save(reservation);
    }

    // 현재 예약 에서 상태만 변경
    @Override
    public Reservation updateStateReservation(Long reservationId, ReservationStateDto stateDto) {
        Reservation reservation = findReservationById(reservationId);
        reservation.setState(stateDto.getState());
        return reservationDao.save(reservation);
    }


    @Override
    public Reservation updateRoomReservation(Long reservationId, ReservationRoomDto roomDto) {

        Reservation reservation = findReservationById(reservationId);

        Long roomId = roomDto.getRoomId();
        LocalDateTime startDateTime = reservation.getReservationStartTime();
        LocalDateTime endDateTime = reservation.getReservationEndTime();

        // todo : 검증 하기
        validateRoomAvailability(roomId,startDateTime,endDateTime);

        reservation.setRoom(roomService.findRoomById(roomId));
        return reservationDao.save(reservation);
    }


    @Override
    public void deleteReservationById(Long reservationId) {
        reservationDao.deleteById(reservationId);
    }

    private void validateRoomAvailability(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        boolean isOperating = isOperating(roomId, startDateTime, endDateTime);
        boolean hasNoOverlappingReservations = isRoomNotOverlapping(roomId, startDateTime, endDateTime);

        // 예약 가능 여부 확인 로직
        boolean isRoomAvailable = isOperating && hasNoOverlappingReservations;

        if (!isRoomAvailable) {
            // 예약 불가
            if (!isOperating) {
                throw new RoomNotOperatingException(roomId, startDateTime, endDateTime);
            } else if (!hasNoOverlappingReservations) {
                throw new OverlappingReservationException(roomId, startDateTime, endDateTime);
            }
        }
    }

    private boolean isOperating(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 룸의 운영 시간인지 확인
        return roomService.isRoomAvailable(roomId, startDateTime, endDateTime);
    }

    private boolean isRoomNotOverlapping(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 다른 예약과 겹치지 않는지 확인
        return reservationDao.findOverlappingReservations(roomId, startDateTime, endDateTime).isEmpty();
    }

}
