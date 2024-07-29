package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.*;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import com.test.studyroomreservationsystem.dto.reservation.*;
import com.test.studyroomreservationsystem.exception.*;

import com.test.studyroomreservationsystem.exception.invalidvalue.InvalidReservationIdException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final RoomPartitionService partitionService;
    private final UserService userService;
    private final RoomOperationPolicyScheduleService scheduleService;
    @Value("${spring.service.noShowCntMonth}") private Long noShowCntMonth;
    @Value("${spring.service.noShowLimit}") private int noShowLimit;
    @Value("${spring.service.reservationLimit}") private int reservationLimit;
    @Value("${spring.service.reservationLimitToday}") private int reservationLimitToday;

    @Override
    public Reservation createReservation(ReservationRequestDto reservationRequestDto, User user) {
        Long roomPartitionId = reservationRequestDto.getRoomPartitionId();
        Long userId = user.getUserId();
        Instant startDateTime = reservationRequestDto.getStartDateTime();
        Instant endDateTime = reservationRequestDto.getEndDateTime();

        // 검증
        validateRoomAvailability(userId, roomPartitionId, startDateTime, endDateTime);

        Reservation reservationEntity = reservationRequestDto.toEntity(
                user,partitionService.findRoomPartitionById(roomPartitionId)
        );

        return reservationRepository.save(reservationEntity);

    }

    @Override
    public Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    @Override // todo 검사
    public Reservation findRecentReservationByUserId(Long userId) {
        return reservationRepository.findTopByUserUserIdOrderByReservationStartTimeDesc(userId)
                .orElseThrow(() -> new ReservationNotFoundException("Recent reservation not found for user " + userId));
    }

    @Override
    public List<Reservation> findAllReservation() {
        return reservationRepository.findAll();
    }

    // 해당 유저의 모든 reservation 로그 찾기
    @Override
    public List<Reservation> findAllReservationByUser(Long userId) {
        User user = userService.findUserById(userId);
        return reservationRepository.findAllByUser(user).orElseThrow(
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
        reservationRepository.deleteById(reservationId);
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
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public List<Reservation> findValidReservations(
            Long userId,
            List<Long> roomPartitionIds,
            Instant nowTime,
            Long allowedStartMinute) {
        String notVisitedState = ReservationState.NOT_VISITED.toString();
        return reservationRepository.findValidReservations(userId, roomPartitionIds, nowTime, allowedStartMinute,notVisitedState)
                .orElseThrow(ReservationNotFoundException::new);
    }


    @Override
    public List<PartitionsReservationResponseDto> getReservationsByAllPartitionsAndDate(LocalDate date) {
        List<RoomPartition> partitions = partitionService.findAllRoomPartition();
        ArrayList<PartitionsReservationResponseDto> responseList = new ArrayList<>();

        for (RoomPartition partition : partitions) {
            try {
                Room room = partition.getRoom();
                RoomOperationPolicySchedule schedule = scheduleService.findScheduleByRoomAndDate(room, date);
                RoomOperationPolicy policy = schedule.getRoomOperationPolicy();

                Instant operationStartTime = DateTimeUtil.convertKstToUtc(
                        date.atTime(policy.getOperationStartTime())
                ).atZone(ZoneOffset.UTC).toInstant();

                Instant operationEndTime = DateTimeUtil.convertKstToUtc(
                        date.atTime(policy.getOperationEndTime())
                ).atZone(ZoneOffset.UTC).toInstant();

                // 각 파티션의 예약들
                List<Reservation> reservations = reservationRepository.findOverlappingReservations(
                        partition.getRoomPartitionId(),
                        operationStartTime,
                        operationEndTime);

                List<PartitionsReservationResponseDto.TimeRange> reservationTimes = reservations.stream()
                        .map(reservation -> new PartitionsReservationResponseDto.TimeRange(
                                reservation.getReservationId(),
                                reservation.getReservationStartTime(),
                                reservation.getReservationEndTime()))
                        .toList();

                responseList.add(PartitionsReservationResponseDto.builder()
                        .partitionId(partition.getRoomPartitionId())
                        .roomId(room.getRoomId())
                        .roomName(room.getRoomName())
                        .partitionNumber(partition.getPartitionNumber())
                        .policy(policy)
                        .timeline(reservationTimes)
                        .build());
            } catch (ScheduleNotFoundException e) {
                // RoomOperationPolicy가 설정되지 않은 경우 무시하고 넘어갑니다.
            }
        }
        return responseList;
    }


    @Override
    public SpecificRoomsReservationsDto getReservationsByPartitionsAndDate(List<Long> partitionIds, LocalDate date) {
        Instant startTime = DateTimeUtil.getStartOfDateInstant(date);
        Instant endTime = DateTimeUtil.getEndOfDateInstant(date);

        List<Reservation> reservations
                = reservationRepository.findByRoomPartitionRoomPartitionIdInAndReservationStartTimeBetween(partitionIds, startTime, endTime);

        List<SpecificRoomsReservationsDto.RoomReservation> roomReservations
                = responseDtoFrom(reservations);

        return new SpecificRoomsReservationsDto(roomReservations);
    }

    private void validateRoomAvailability(Long userId,Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
        //  이용 불가 한 학생
        if (isUserBlocked(userId)) {
            throw new NoShowLimitExceededException();
        }
        Long roomId = partitionService.findRoomPartitionById(roomPartitionId).getRoom().getRoomId();
        //  운영이 하지 않음 (운영 정책 없음), 운영이 종료 되었음 (운영 정책 있음)
        isOperating(roomId, startDateTime, endDateTime);

        // 잘못된 예약 : 시간 : 예약당 최대 시간 초과
        if (isMaxReservationTimeExceeded(roomId, startDateTime, endDateTime)) {
            throw new ExceedingMaxReservationTimeException();
        }

        // 잘못된 예약 : 예약 시작 < 예약끝 인지 확인
        if (isInvalidReservationTime(startDateTime, endDateTime)) {
            throw new InvalidReservationTimeException();
        }
        // 현재 가능한 예약 초과 : 사용자는 2개 이상의 예약(NOT_VISITED)을 보유할 수 없음
        if (isTooManyCurrent(userId)) {
            throw new TooManyCurrentReservationsException(reservationLimit);
        }
        // 오늘 가능한 예약 초과 : 사용자는 하루에 2개 까지 예약을 생성할 수 있음
        if (isTooManyToday(userId)) {
            throw new TooManyTodayReservationsException(reservationLimitToday);
        }
        // 이미 다른 사용자의 예약이 있음
        if (isReservationOverlapping(roomPartitionId, startDateTime, endDateTime)) {
            throw new OverlappingReservationException(partitionService.findRoomPartitionById(roomPartitionId));
        }

    }
    /*              오늘 가능한 예약 초과                  */
    @Override
    public List<Reservation> getReservationsByUserIdAndToday(Long userId) {
        return reservationRepository.findByUserIdAndReservationStartTime(
                userId,
                DateTimeUtil.getStartOfTodayInstant(),
                DateTimeUtil.getEndOfTodayInstant()
        );
    }
    private boolean isTooManyToday(Long userId) {
        int size = getReservationsByUserIdAndToday(userId).size();
        return size >= reservationLimitToday;
    }

    /*              현재 가능한 예약 초과                  */
    @Override
    public List<Reservation> getCurrentReservations(Long userId) {
        Instant nowTime = Instant.now();
        // 현재 시점 보다 이후 reservationEndTime 이며 NOT_VISITED 인 예약 가져오기
        return reservationRepository.findCurrentReservations(userId, nowTime);
    }
    private boolean isTooManyCurrent(Long userId) {
        // 현재 시점 이후와
        int size = getCurrentReservations(userId).size();
        return size >= reservationLimit;
    }


    /*              노쇼 횟수 초과                  */
    @Override
    public List<Reservation> getNoShowReservations(Long userId) {

        ZonedDateTime endTime = DateTimeUtil.getOneDayBefore(DateTimeUtil.getEndOfTodayZoned());
        ZonedDateTime startTime = DateTimeUtil.getMonthBefore(endTime, noShowCntMonth);
        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();

        return reservationRepository.countNoShowsByUserIdAndPeriod(userId, startInstant, endInstant);
    }
    private boolean isUserBlocked(Long userId){
        List<Reservation> reservations = getNoShowReservations(userId);
        int noShowCnt = reservations.size();
        
        return noShowCnt > noShowLimit;
    }


    /*              잘못된 예약 시간인지?                  */
    private boolean isInvalidReservationTime(Instant startDateTime, Instant endDateTime) {
        return startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime);
    }


    /*          룸이 운영을 하는지? && 운영이 종료 되었는지?               */
    private void isOperating(Long roomId, Instant startDateTime, Instant endDateTime) {
        roomService.isRoomAvailable(roomId, startDateTime, endDateTime);
    }


    /*                다른 예약과 겹치지 않는지 확인                  */
    private boolean isReservationOverlapping(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
        List<Reservation> reservations = reservationRepository.findOverlappingReservations(roomPartitionId, startDateTime, endDateTime);
        return !reservations.isEmpty();
    }


    /*             예약 시간이 정책 한도를 초과하는지?               */
    private boolean isMaxReservationTimeExceeded(Long roomId, Instant startDateTime, Instant endDateTime) {
        Room room = roomService.findRoomById(roomId);

        long reservationMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
        LocalDate date = endDateTime.atZone(ZoneOffset.UTC).toLocalDate();
        RoomOperationPolicySchedule schedule = scheduleService.findScheduleByRoomAndDate(room, date);
        Integer eachMaxMinute = schedule.getRoomOperationPolicy().getEachMaxMinute();

        return reservationMinutes > eachMaxMinute;
    }

}
