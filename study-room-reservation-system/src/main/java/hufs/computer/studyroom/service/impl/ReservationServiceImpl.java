package hufs.computer.studyroom.service.impl;

import com.computer.studyroom.domain.entity.*;
import hufs.computer.studyroom.domain.entity.User.ServiceRole;
import hufs.computer.studyroom.domain.repository.ReservationRepository;
import com.computer.studyroom.dto.reservation.*;
import com.computer.studyroom.exception.*;

import hufs.computer.studyroom.exception.invalidvalue.InvalidReservationIdException;
import hufs.computer.studyroom.exception.notfound.ReservationHistoryNotFoundException;
import hufs.computer.studyroom.exception.notfound.ReservationNotFoundException;
import hufs.computer.studyroom.exception.notfound.ScheduleNotFoundException;
import hufs.computer.studyroom.exception.reservation.ExceedingMaxReservationTimeException;
import hufs.computer.studyroom.exception.reservation.InvalidReservationTimeException;
import hufs.computer.studyroom.exception.reservation.OverlappingReservationException;
import hufs.computer.studyroom.exception.reservation.TooManyCurrentReservationsException;
import hufs.computer.studyroom.exception.reservation.TooManyTodayReservationsException;
import hufs.computer.studyroom.exception.user.NotPossibleDeleteException;
import hufs.computer.studyroom.security.CustomUserDetails;
import com.computer.studyroom.service.*;
import hufs.computer.studyroom.domain.entity.*;
import hufs.computer.studyroom.dto.reservation.PartitionsReservationResponseDto;
import hufs.computer.studyroom.dto.reservation.ReservationInfoUpdateRequestDto;
import hufs.computer.studyroom.dto.reservation.ReservationRequestDto;
import hufs.computer.studyroom.dto.reservation.SpecificRoomsReservationsDto;
import hufs.computer.studyroom.exception.AccessDeniedException;
import hufs.computer.studyroom.exception.NoShowLimitExceededException;
import hufs.computer.studyroom.service.*;
import hufs.computer.studyroom.service.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Value("${spring.service.noShowBlockMonth}") private Long noShowBlockMonth;
    @Value("${spring.service.noShowLimit}") private int noShowLimit;
    @Value("${spring.service.reservationLimit}") private int reservationLimit;
    @Value("${spring.service.reservationLimitToday}") private int reservationLimitToday;

    @Transactional
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

    @Override
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
    public List<Reservation> findAllReservationByAdmin(String serial, CustomUserDetails currentUser) {
        User admin = currentUser.getUser();
        if (admin.getServiceRole() != null && admin.getServiceRole() != ServiceRole.ADMIN) {
            throw new AccessDeniedException();
        }
        User foundUser = userService.findBySerial(serial);
        return reservationRepository.findAllByUser(foundUser).orElseThrow(
                () -> new ReservationHistoryNotFoundException(serial));
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
        if (reservation.getState() == Reservation.ReservationState.VISITED) {
            throw new NotPossibleDeleteException();
        }
        if (reservation.getReservationStartTime().isBefore(Instant.now())) {
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
        if (user.getServiceRole() != null && user.getServiceRole() != ServiceRole.ADMIN) {
            throw new AccessDeniedException();
        }
        reservationRepository.deleteById(reservationId);
    }
    @Override
    public Reservation updateReservationInfo(Long reservationId, ReservationInfoUpdateRequestDto requestDto) {
        Reservation reservation = findReservationById(reservationId);
        requestDto.toEntity(reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> findValidReservations(
            Long userId,
            List<Long> roomPartitionIds,
            Instant nowTime,
            Long allowedStartMinute) {
        String notVisitedState = Reservation.ReservationState.NOT_VISITED.toString();
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
        Instant startTime = DateTimeUtil.getInstantStartOfDate(date);
        Instant endTime = DateTimeUtil.getInstantEndOfDate(date);

        List<Reservation> reservations
                = reservationRepository.findByRoomPartitionRoomPartitionIdInAndReservationStartTimeBetween(partitionIds, startTime, endTime);

        List<SpecificRoomsReservationsDto.RoomReservation> roomReservations
                = responseDtoFrom(reservations);

        return new SpecificRoomsReservationsDto(roomReservations);
    }

    private void validateRoomAvailability(Long userId,Long roomPartitionId, Instant startDateTime, Instant endDateTime) {

        List<Reservation> noShowReservations = getNoShowReservations(userId);
        //  이용 불가 한 학생
        if (isUserBlocked(noShowReservations)) {
//            noshowReservation 중에서 가장 마지막으로 생성된 예약을 가져온다.
            Instant startBlockTime = getLatestNoShowTime(noShowReservations);
//            가장 마지막으로 생성된 예약의 날짜부터 noShowCntMonth 달 후 까지 블락기간이다.
//            즉,  noShowCntMonth 달 후, 다음날 부터는 이용가능
            Instant endBlockTime = DateTimeUtil.getInstantOneDayAfterStartOfDay(DateTimeUtil.getInstantMonthAfter(startBlockTime, noShowBlockMonth)).minusNanos(1);

//            블락기간이 끝났다면 noshowReservation 들의 state를 PROCESSED 로 변경한다.
            if (DateTimeUtil.getInstantCurrent().isAfter(endBlockTime)) {
                changeStateToProcessd(noShowReservations);
            }
//            블락기간이 끝나지 않아다면, NoShowLimitExceededException 예외를 터트린다.
            else {
                throw new NoShowLimitExceededException(startBlockTime,endBlockTime);
            }

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

    private static Instant getLatestNoShowTime(List<Reservation> noShowReservations) {
        return noShowReservations.stream()
                .map(Reservation::getCreateAt)
                .max(Instant::compareTo)
                .orElseThrow(ReservationNotFoundException::new);
    }

    @Transactional
    private void changeStateToProcessd(List<Reservation> noShowReservations) {
        noShowReservations.forEach(reservation -> {
            reservation.setState(Reservation.ReservationState.PROCESSED);
        });
        reservationRepository.saveAll(noShowReservations);
    }

    /*              오늘 가능한 예약 초과                  */
    // KST ->  UTC
    @Override
    public List<Reservation> getReservationsByUserIdAndToday(Long userId) {
        Instant start = DateTimeUtil.getInstantStartOfToday();
        Instant end = DateTimeUtil.getInstantEndOfToday();
        return reservationRepository.findByUserIdAndReservationStartTime(userId, start, end);
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
        Reservation recent;
        try {
            recent = findRecentReservationByUserId(userId);
        } catch (ReservationNotFoundException e) {
            return new ArrayList<>();
        }
        Instant reservationEndTime = recent.getReservationEndTime();
        Instant now = Instant.now();
        Instant endInstant;
    /*
    * 노쇼 찾기
    * 현재     →  예약 시작, 예약 종료     | 시작 전 으로 찾기
    * 예약 시작 →  현재     →   예약 종료  | 시작 전 으로 찾기
    * 예약 시작 →  예약 종료 →  현재       | 현재     로 찾기
    * */
        if (reservationEndTime.isAfter(now)) {
            endInstant = recent.getReservationStartTime().minusMillis(1);
        } else {
            endInstant = now;
        }

        return reservationRepository.countNoShowsByUserIdAndPeriod(userId, endInstant);
    }

    private boolean isUserBlocked(List<Reservation> reservations){
        int noShowCnt = reservations.size();
        //        n 회 이상이면 블락
        return noShowCnt >= noShowLimit;
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
    /* 사용자가 예약하려는 시간 startDateTime ~ endDateTime */
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
