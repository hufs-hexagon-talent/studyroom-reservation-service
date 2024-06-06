package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.domain.entity.*;
import com.test.studyroomreservationsystem.dto.reservation.*;
import com.test.studyroomreservationsystem.exception.*;
import com.test.studyroomreservationsystem.exception.invaildvalue.ReservationIdInvaildValueException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationHistoryNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationNotFoundException;
import com.test.studyroomreservationsystem.exception.reservation.ExceedingMaxReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.InvalidReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.OverlappingReservationException;
import com.test.studyroomreservationsystem.exception.reservation.PastReservationTimeException;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final RoomService roomService;
    private final UserService userService;
    private final RoomOperationPolicyScheduleService scheduleService;

//    @Value("${spring.service.allowedEndMinute}")
//    private Integer allowedEndMinute;
//    @Value("${spring.service.allowedStartMinute}")
//    private Integer allowedStartMinute;
//
//    private Duration allowedStartTime;
//    private Duration allowedEndTime;
//
//    @PostConstruct
//    public void init() {
//        this.allowedStartTime = Duration.ofMinutes(allowedStartMinute);
//        this.allowedEndTime = Duration.ofMinutes(allowedEndMinute);
//    }
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
        Instant startDateTime = reservationRequestDto.getStartDateTime();
        Instant endDateTime = reservationRequestDto.getEndDateTime();

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
    public void deleteReservation(Long reservationId, CustomUserDetails currentUser) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) {
            throw new ReservationIdInvaildValueException(reservationId);
        }
        User user = currentUser.getUser();
        if (!reservation.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException();
        }
        reservationDao.deleteById(reservationId);
    }

    public List<Reservation> findByUserIdAndRoomIdAndStartTimeBetween(Long userId, List<Long> roomIds, Instant startTime, Instant endTime) {
        return reservationDao.findByUserIdAndRoomIdsAndStartTimeBetween(userId, roomIds, startTime, endTime).orElseThrow(
                ReservationNotFoundException::new
        );
    }

    private void validateRoomAvailability(Long roomId, Instant startDateTime, Instant endDateTime) {
        boolean withinMaxReservationTime = isWithinMaxReservationTime(roomId, startDateTime, endDateTime);
//        boolean isPastTime = isPastTime(startDateTime);
        boolean hasNoOverlappingReservations = isRoomNotOverlapping(roomId, startDateTime, endDateTime);
        boolean isInvalidReservationTime = (startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime));

        //  운영이 하지 않음 (운영 정책 없음), 운영이 종료 되었음 (운영 정책 있음)
        isOperating(roomId, startDateTime, endDateTime);

        if (!withinMaxReservationTime) {
            throw new ExceedingMaxReservationTimeException();
        }
        // 이미 예약이 있음
        if (!hasNoOverlappingReservations) {
            throw new OverlappingReservationException(roomService.findRoomById(roomId), startDateTime, endDateTime);
        }
        // 예약 시간이 과거임
//        if (isPastTime) {
//            throw new PastReservationTimeException(startDateTime, endDateTime);
//        }
        // 잘못된 예약 시간인지 확인
        if (isInvalidReservationTime) {
            throw new InvalidReservationTimeException();
        }

    }

    private void isOperating(Long roomId, Instant startDateTime, Instant endDateTime) {
        // 룸이 운영을 하는지? && 운영이 종료 되었는지?
        roomService.isRoomAvailable(roomId, startDateTime, endDateTime);
    }

    private boolean isRoomNotOverlapping(Long roomId, Instant startDateTime, Instant endDateTime) {
        // 다른 예약과 겹치지 않는지 확인
        return reservationDao.findOverlappingReservations(roomId, startDateTime, endDateTime).isEmpty();
    }

    private boolean isWithinMaxReservationTime(Long roomId, Instant startDateTime, Instant endDateTime) {
        Room room = roomService.findRoomById(roomId);

        long reservationMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
        LocalDate date = endDateTime.atZone(ZoneOffset.UTC).toLocalDate();
        RoomOperationPolicySchedule schedule = scheduleService.findScheduleByRoomAndDate(room, date);
        Integer eachMaxMinute = schedule.getRoomOperationPolicy().getEachMaxMinute();

        return reservationMinutes <= eachMaxMinute;
    }

//    private boolean isPastTime(Instant startDateTime) {
//        // 과거 시간이 아닌지 확인 (allowedStartTime을 고려)
//        Instant now = Instant.now();
//        Instant allowedStartDateTime = now.minus(allowedStartTime);
//        return startDateTime.isBefore(allowedStartDateTime);
//    }

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

}
