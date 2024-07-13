package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.*;
import com.test.studyroomreservationsystem.dto.reservation.*;
import com.test.studyroomreservationsystem.exception.*;

import com.test.studyroomreservationsystem.exception.invaildvalue.InvalidReservationIdException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationHistoryNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.ScheduleNotFoundException;
import com.test.studyroomreservationsystem.exception.reservation.ExceedingMaxReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.InvalidReservationTimeException;
import com.test.studyroomreservationsystem.exception.reservation.OverlappingReservationException;
import com.test.studyroomreservationsystem.exception.reservation.TooManyCurrentReservationsException;
import com.test.studyroomreservationsystem.exception.reservation.TooManyTodayReservationsException;
import com.test.studyroomreservationsystem.exception.user.NotPossibleDeleteException;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

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
    @Value("${spring.service.reservationLimit}")
    private int reservationLimit;
    @Value("${spring.service.reservationLimitToday}")
    private int reservationLimitToday;
    private static final int ONE_DAY_OFFSET = 1;

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
    public void deleteReservationBySelf(Long reservationId, CustomUserDetails currentUser) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) {
            throw new InvalidReservationIdException(reservationId);
        }
        User user = currentUser.getUser();
        if (!reservation.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException();
        }
        if (reservation.getState() == ReservationState.VISITED) {
            throw new NotPossibleDeleteException();
        }
        reservationDao.deleteById(reservationId);
    }

    @Override
    public void deleteReservationByAdmin(Long reservationId, CustomUserDetails currentUser) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) {
            throw new InvalidReservationIdException(reservationId);
        }
        User user = currentUser.getUser();
        if (Boolean.FALSE.equals(user.getIsAdmin())) {
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
    public List<Reservation> getNotVisitedReservationsAfterNow(Long userId) {
        Instant now = Instant.now();
        // 현재 시점 으로 이후로 NOT_VISITED 인 예약 가져오기
        return reservationDao.getNotVisitedReservationsAfterNow(userId, now);
    }


    @Override
    public List<Reservation> countNoShowsByUserIdAndPeriod(Long userId) {
        ZonedDateTime endTime = ZonedDateTime.now(ZoneOffset.UTC);
        endTime = endTime.minusDays(ONE_DAY_OFFSET);
        ZonedDateTime startTime = endTime.minusMonths(noShowCntMonth);
        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();

        return reservationDao.countNoShowsByUserIdAndPeriod(userId, startInstant, endInstant);
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
                        .toList();

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
                        reservation.getUser().getName(),
                        reservation.getState(),
                        reservation.getReservationStartTime(),
                        reservation.getReservationEndTime()))
                .toList();

        return new SpecificRoomsReservationsDto(roomReservations);
    }

    @Override
    public List<Reservation> getReservationsByUserIdAndToday(Long userId) {
        return reservationDao.findByUserIdAndTodayDate(userId);
    }

    private void validateRoomAvailability(Long userId,Long roomId, Instant startDateTime, Instant endDateTime) {
        //  이용 불가 한 학생
        if (isUserBlocked(userId)) {
            throw new NoShowLimitExceededException();
        }

        //  운영이 하지 않음 (운영 정책 없음), 운영이 종료 되었음 (운영 정책 있음)
        isOperating(roomId, startDateTime, endDateTime);

        // 잘못된 예약 : 시간 : 예약당 최대 시간 초과
        if (!isWithinMaxReservationTime(roomId, startDateTime, endDateTime)) {
            throw new ExceedingMaxReservationTimeException();
        }

        // 잘못된 예약 : 예약 시작 < 예약끝 인지 확인
        if (isInvalidReservationTime(startDateTime, endDateTime)) {
            throw new InvalidReservationTimeException();
        }
        // 현재 가능한 예약 초과 : 사용자는 1개 이상의 예약(NOT_VISITED)을 보유할 수 없음
        boolean tooManyCurrent = isTooManyCurrent(userId);
        if (tooManyCurrent) {
            throw new TooManyCurrentReservationsException(reservationLimit);
        }
        // 오늘 가능한 예약 초과 : 사용자는 하루에 2개 까지 예약을 생성할 수 있음
        boolean tooManyToday = isTooManyToday(userId);
        if (tooManyToday) {
            throw new TooManyTodayReservationsException(reservationLimitToday);
        }
        // 이미 다른 사용자의 예약이 있음
        if (!isRoomNotOverlapping(roomId, startDateTime, endDateTime)) {
            throw new OverlappingReservationException(roomService.findRoomById(roomId), startDateTime, endDateTime);
        }

    }

    private boolean isTooManyToday(Long userId) {
        int size = getReservationsByUserIdAndToday(userId).size();
        return size >= reservationLimitToday;
    }

    private boolean isTooManyCurrent(Long userId) {
        int size = getNotVisitedReservationsAfterNow(userId).size();
        return size >= reservationLimit;
    }

    private boolean isInvalidReservationTime(Instant startDateTime, Instant endDateTime) {
        return startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime);
    }

    private boolean isUserBlocked(Long userId){
        List<Reservation> reservations = countNoShowsByUserIdAndPeriod(userId);
        int noShowCnt = reservations.size();
        
        return noShowCnt > noShowLimit;
    }

    // 룸이 운영을 하는지? && 운영이 종료 되었는지?
    private void isOperating(Long roomId, Instant startDateTime, Instant endDateTime) {
        roomService.isRoomAvailable(roomId, startDateTime, endDateTime);
    }

    // 다른 예약과 겹치지 않는지 확인
    private boolean isRoomNotOverlapping(Long roomId, Instant startDateTime, Instant endDateTime) {
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
