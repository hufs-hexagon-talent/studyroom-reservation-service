package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.dao.RoomOperationPolicyScheduleDao;
import com.test.studyroomreservationsystem.domain.entity.*;
import com.test.studyroomreservationsystem.dto.reservation.*;
import com.test.studyroomreservationsystem.exception.*;
import com.test.studyroomreservationsystem.exception.notfound.ReservationHistoryNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationNotFoundException;
import com.test.studyroomreservationsystem.exception.reservation.ExceedingMaxReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.OverlappingReservationException;
import com.test.studyroomreservationsystem.exception.reservation.PastReservationTimeException;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final RoomService roomService;
    private final UserService userService;
    private final RoomOperationPolicyScheduleService scheduleService;
    @Autowired

    public ReservationServiceImpl(ReservationDao reservationDao, RoomService roomService, UserService userService, RoomOperationPolicyScheduleService scheduleService) {
        this.reservationDao = reservationDao;
        this.roomService = roomService;
        this.userService = userService;
        this.scheduleService = scheduleService;
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
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
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

        validateRoomAvailability(roomId, startDateTime, endDateTime);
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
        validateRoomAvailability(roomId, startDateTime, endDateTime);

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

        validateRoomAvailability(roomId, startDateTime, endDateTime);

        reservation.setRoom(roomService.findRoomById(roomId));
        return reservationDao.save(reservation);
    }


    @Override
    public void deleteReservation(Long reservationId, CustomUserDetails currentUser) {
        Reservation reservation = findReservationById(reservationId);
        User user = currentUser.getUser();
        if (!reservation.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException();
        }
        reservationDao.deleteById(reservationId);
    }

    @Override
    public ReservationRequestDto requestDtoFrom(Reservation reservation) {
        return ReservationRequestDto.builder()
                .roomId(reservation.getRoom().getRoomId())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .build();
    }

    @Override
    public ReservationResponseDto responseDtoFrom(Reservation reservation) {
        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .roomId(reservation.getRoom().getRoomId())
                .roomName(reservation.getRoom().getRoomName())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .build();
    }


    private void validateRoomAvailability(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        boolean isFutureTime = isFutureTime(startDateTime, endDateTime);
        boolean hasNoOverlappingReservations = isRoomNotOverlapping(roomId, startDateTime, endDateTime);
        boolean withinMaxReservationTime = isWithinMaxReservationTime(roomId, startDateTime, endDateTime);
        // 예약시간이 과거임
        if (!isFutureTime) {
            throw new PastReservationTimeException(startDateTime, endDateTime);
        }

        //  운영이 하지 않음 (운영 정책 없음), 운영이 종료 되었음 (운영 정책 있음)
        isOperating(roomId, startDateTime, endDateTime);

        // 이미 예약이 있음
        if (!hasNoOverlappingReservations) {
            throw new OverlappingReservationException(roomService.findRoomById(roomId), startDateTime, endDateTime);
        }
        // todo : 예약 시간(each_max_minute) 초과 !
        if (!withinMaxReservationTime) {
            throw new ExceedingMaxReservationTimeException();
        }

    }


    private void isOperating(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 룸이 운영을 하는지? && 운영이 종료 되었는지?
        roomService.isRoomAvailable(roomId, startDateTime, endDateTime);
    }

    private boolean isRoomNotOverlapping(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 다른 예약과 겹치지 않는지 확인
        return reservationDao.findOverlappingReservations(roomId, startDateTime, endDateTime).isEmpty();
    }

    private boolean isFutureTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 과거 시간이 아닌지 확인
        LocalDateTime now = LocalDateTime.now();
        return startDateTime.isAfter(now) && endDateTime.isAfter(now);
    }
    private boolean isWithinMaxReservationTime(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Room room = roomService.findRoomById(roomId);
        long reservationMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
        LocalDate date = endDateTime.toLocalDate();
        RoomOperationPolicySchedule schedule = scheduleService.findScheduleByRoomAndDate(room, date);
        Integer eachMaxMinute = schedule.getRoomOperationPolicy().getEachMaxMinute();

        return reservationMinutes <= eachMaxMinute;
    }
}
