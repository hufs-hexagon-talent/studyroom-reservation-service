package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.domain.entity.*;
import com.test.studyroomreservationsystem.dto.reservation.*;
import com.test.studyroomreservationsystem.exception.*;

import com.test.studyroomreservationsystem.exception.invaildvalue.ReservationIdInvalidValueException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationHistoryNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.ScheduleNotFoundException;
import com.test.studyroomreservationsystem.exception.reservation.ExceedingMaxReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.InvalidReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.OverlappingReservationException;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final RoomService roomService;
    private final UserService userService;
    private final RoomOperationPolicyScheduleService scheduleService;
    @Value("${spring.service.noShowCntMonth}")
    private Long noShowCntMonth;
    @Value("${spring.service.noShowLimit}")
    private int noShowLimit;

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
        Long userId = user.getUserId();
        Instant startDateTime = reservationRequestDto.getStartDateTime();
        Instant endDateTime = reservationRequestDto.getEndDateTime();

        // 검증
        validateRoomAvailability(userId,roomId, startDateTime, endDateTime);

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
            throw new ReservationIdInvalidValueException(reservationId);
        }
        User user = currentUser.getUser();
        if (!reservation.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException();
        }
        reservationDao.deleteById(reservationId);
    }

    @Override
    public List<Reservation> findByUserIdAndRoomIdAndStartTimeBetween(Long userId, List<Long> roomIds, Instant startTime, Instant endTime) {
        return reservationDao.findByUserIdAndRoomIdsAndStartTimeBetween(userId, roomIds, startTime, endTime).orElseThrow(
                ReservationNotFoundException::new
        );
    }

    @Override
    public List<Reservation> countNoShowsByUserIdAndPeriod(Long userId) {
        ZonedDateTime endTime = ZonedDateTime.now(ZoneOffset.UTC);

        ZonedDateTime startTime = endTime.minus(noShowCntMonth, ChronoUnit.MONTHS);
        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();
        List<Reservation> reservations = reservationDao.countNoShowsByUserIdAndPeriod(userId, startInstant, endInstant);


        return reservations;
    }


    @Override
    public List<RoomsReservationResponseDto> getReservationsByAllRoomsAndDate(LocalDate date) {
        List<Room> rooms = roomService.findAllRoom();
        ArrayList<RoomsReservationResponseDto> responseList = new ArrayList<>();

        for (Room room : rooms) {
            try {
                RoomOperationPolicySchedule schedule = scheduleService.findScheduleByRoomAndDate(room, date);
                RoomOperationPolicy policy = schedule.getRoomOperationPolicy();

                LocalTime operationStartTime = policy.getOperationStartTime();
                LocalTime operationEndTime = policy.getOperationEndTime();

                Instant operationStartDateTime = date.atTime(operationStartTime).toInstant(ZoneOffset.UTC);
                Instant operationEndDateTime = date.atTime(operationEndTime).toInstant(ZoneOffset.UTC);

                // 각 룸의 예약들
                List<Reservation> reservations = reservationDao.findOverlappingReservations(room.getRoomId(), operationStartDateTime, operationEndDateTime);
                List<RoomsReservationResponseDto.TimeRange> reservationTimes = reservations.stream()
                        .map(reservation -> new RoomsReservationResponseDto.TimeRange(
                                reservation.getReservationId(),
                                reservation.getReservationStartTime(),
                                reservation.getReservationEndTime()))
                        .collect(Collectors.toList());

                responseList.add(new RoomsReservationResponseDto(
                        room.getRoomId(),
                        room.getRoomName(),
                        policy,
                        reservationTimes));
            } catch (ScheduleNotFoundException e) {
                // RoomOperationPolicy가 설정되지 않은 경우 무시하고 넘어갑니다.
            }
        }
        return responseList;
    }




    @Override
    public SpecificRoomsReservationsDto getReservationsByRoomsAndDate(List<Long> roomIds, LocalDate date) {
        Instant startTime = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endTime = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<Reservation> reservations = reservationDao.findByRoomIdsAndStartTimeBetween(roomIds, startTime, endTime);

        List<SpecificRoomsReservationsDto.RoomReservation> roomReservations = reservations.stream()
                .map(reservation -> new SpecificRoomsReservationsDto.RoomReservation(
                        reservation.getReservationId(),
                        reservation.getRoom().getRoomId(),
                        reservation.getRoom().getRoomName(),
                        reservation.getUser().getUserId(),
                        reservation.getState(),
                        reservation.getReservationStartTime(),
                        reservation.getReservationEndTime()))
                .collect(Collectors.toList());

        return new SpecificRoomsReservationsDto(roomReservations);
    }


    private void validateRoomAvailability(Long userId,Long roomId, Instant startDateTime, Instant endDateTime) {
        boolean withinMaxReservationTime = isWithinMaxReservationTime(roomId, startDateTime, endDateTime);
//        boolean isPastTime = isPastTime(startDateTime);
        boolean hasNoOverlappingReservations = isRoomNotOverlapping(roomId, startDateTime, endDateTime);
        boolean isInvalidReservationTime = (startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime));
        //  이용 불가 한 학생
        if (isNoneBlocking(userId)) {
            // todo 예외 처리 끝까지 ..
            throw new NoShowLimitExceededException();
        }
        //  운영이 하지 않음 (운영 정책 없음), 운영이 종료 되었음 (운영 정책 있음)
        isOperating(roomId, startDateTime, endDateTime);

        if (!withinMaxReservationTime) {
            throw new ExceedingMaxReservationTimeException();
        }
        // 이미 예약이 있음
        if (!hasNoOverlappingReservations) {
            throw new OverlappingReservationException(roomService.findRoomById(roomId), startDateTime, endDateTime);
        }

        // 잘못된 예약 시간인지 확인
        if (isInvalidReservationTime) {
            throw new InvalidReservationTimeException();
        }

    }
    private boolean isNoneBlocking(Long userId){
        List<Reservation> reservations = countNoShowsByUserIdAndPeriod(userId);
        int noShowCnt = reservations.size();
        
        return noShowCnt > noShowLimit;
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

    @Override
    public ReservationRequestDto requestDtoFrom(Reservation reservation) {
        return ReservationRequestDto.builder()
                .roomId(reservation.getRoom().getRoomId())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .build();
    }

    @Override
    public ReservationInfoResponseDto responseDtoFrom(Reservation reservation) {
        return ReservationInfoResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUser().getUserId())
                .roomId(reservation.getRoom().getRoomId())
                .roomName(reservation.getRoom().getRoomName())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .reservationState(reservation.getState())
                .build();
    }

}
