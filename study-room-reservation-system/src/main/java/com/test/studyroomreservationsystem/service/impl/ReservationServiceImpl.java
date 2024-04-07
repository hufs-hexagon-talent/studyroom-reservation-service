package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.reservation.ReservationDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationStateDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;
import com.test.studyroomreservationsystem.service.ReservationService;
import com.test.studyroomreservationsystem.service.RoomService;
import com.test.studyroomreservationsystem.service.UserService;
import com.test.studyroomreservationsystem.service.exception.ReservationHistoryNotFoundException;
import com.test.studyroomreservationsystem.service.exception.ReservationNotFoundException;
import com.test.studyroomreservationsystem.service.exception.ReservationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Reservation createReservation(ReservationDto reservationDto) {
        Long roomId = reservationDto.getRoomId();
        Long userId = reservationDto.getUserId();
        LocalDateTime startDateTime = reservationDto.getStartDateTime();
        LocalDateTime endDateTime = reservationDto.getEndDateTime();
//        ReservationState state = reservationDto.getState();

        // 예약 가능 여부 확인 로직

        boolean isAvailable = isReservationAvailable(roomId,startDateTime,endDateTime);
        if (!isAvailable) {
            // 에약 불가
            throw new ReservationNotPossibleException(roomId);
        }
        Reservation reservationEntity = reservationDto.toEntity(
                userService.findUserById(userId),
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
    public Reservation updateReservation(Long reservationId, ReservationDto reservationDto) {
        Long roomId = reservationDto.getRoomId();
        LocalDateTime startDateTime = reservationDto.getStartDateTime();
        LocalDateTime endDateTime = reservationDto.getEndDateTime();
        // 예약 가능 여부 확인 로직

        boolean isAvailable = isReservationAvailable(roomId,startDateTime,endDateTime);
        if (!isAvailable) {
            // 에약 변경 불가
            throw new ReservationNotPossibleException(roomId);
        }
        //변경 가능
        Reservation reservation = findReservationById(reservationId);

        User user = userService.findUserById(reservationDto.getUserId());
        reservation.setUser(user);

        Room room = roomService.findRoomById(reservationDto.getRoomId());
        reservation.setRoom(room);

        reservation.setReservationStartTime(reservationDto.getStartDateTime());
        reservation.setReservationEndTime(reservationDto.getEndDateTime());
        reservation.setState(reservationDto.getState());

        return reservationDao.save(reservation);
    }

    // 현재 예약 에서 시간만 변경
    @Override
    public Reservation updateTimeReservation(Long reservationId, ReservationTimeDto timeDto) {
        Reservation reservation = findReservationById(reservationId);

        Long roomId = reservation.getRoom().getRoomId();
        LocalDateTime startDateTime = timeDto.getStartDateTime();
        LocalDateTime endDateTime = timeDto.getEndDateTime();
        // 예약 가능 여부 확인 로직

        boolean isAvailable = isReservationAvailable(roomId,startDateTime,endDateTime);
        if (!isAvailable) {
            // 에약 변경 불가
            throw new ReservationNotPossibleException(roomId);
        }
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

        boolean isAvailable = isReservationAvailable(roomId,startDateTime,endDateTime);
        if (!isAvailable) {
            // 에약 변경 불가
            throw new ReservationNotPossibleException(roomId);
        }
        reservation.setRoom(roomService.findRoomById(roomId));
        return reservationDao.save(reservation);
    }


    @Override
    public void deleteReservationById(Long reservationId) {
        reservationDao.deleteById(reservationId);
    }


    private boolean isReservationAvailable(Long roomId,LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 룸의 운영 시간 인지 확인
        boolean isOperating = roomService.isRoomAvailable(roomId, startDateTime, endDateTime);

        // 다른 예약과 겹치지 않는지 확인 (계층)
        boolean isNotOverlapping = reservationDao.findOverlappingReservations(roomId, startDateTime, endDateTime).isEmpty();

        return isOperating && isNotOverlapping;

    }





//    @Override
//    public List<Reservation> findReservationsByDate(LocalDateTime dateTime) {
//        LocalDate date= dateTime.toLocalDate();
//
//        return reservationDao.findAllReservationsByDate(date);
//    }


}
