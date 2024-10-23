package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.common.error.code.*;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.DateTimeUtil;
import hufs.computer.studyroom.domain.partition.service.PartitionQueryService;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationValidationService {

    @Value("${spring.service.noShowBlockMonth}")
    private Long noShowBlockMonth;
    @Value("${spring.service.noShowLimit}")
    private int noShowLimit;
    @Value("${spring.service.reservationLimit}")
    private int reservationLimit;
    @Value("${spring.service.reservationLimitToday}")
    private int reservationLimitToday;

    private final ReservationRepository reservationRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final PartitionQueryService partitionQueryService;


    public void validateRoomAvailability(Long userId, Long roomPartitionId, Instant startDateTime, Instant endDateTime) {

        // 1. No Show 횟수에 따른 차단 여부 확인
        validateNoShowStatus(userId);

        // 2. 운영 시간 및 예약 가능한 시간 검증
        validateRoomOperationTime(roomPartitionId, startDateTime, endDateTime);

        // 3. 예약 시간 초과 검증
        validateMaxReservationTime(roomPartitionId, startDateTime, endDateTime);

        // 4. 현재 보유 예약 수 검증
        validateCurrentReservations(userId);

        // 5. 당일 예약 수 검증
        validateTodayReservations(userId);

        // 6. 예약 겹침 여부 검증
        validateReservationOverlap(roomPartitionId, startDateTime, endDateTime);
    }

/**
 * 사용자가 No Show 제한에 걸렸는지 검증
 *  노쇼 찾기
 *  현재     →  예약 시작, 예약 종료     | 시작 전 으로 찾기
 *  예약 시작 →  현재     →   예약 종료  | 시작 전 으로 찾기
 *  예약 시작 →  예약 종료 →  현재       | 현재     로 찾기
 */
    private void validateNoShowStatus(Long userId) {
//  가장 마지막으로 생성된 예약의 날짜부터 noShowCntMonth 달 후 까지 블락기간이므로, noShowCntMonth 달 후, 다음날 부터는 이용가능

        List<Reservation> noShowReservations = reservationRepository.findNoShowReservationsByUserId(userId);
        if (noShowReservations.size() >= noShowLimit) {
            Instant latestNoShowTime = noShowReservations.stream()
                    .map(Reservation::getCreateAt)
                    .max(Instant::compareTo)
                    .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));

            Instant blockEndTime = DateTimeUtil.getInstantMonthAfter(latestNoShowTime, noShowBlockMonth);

            if (Instant.now().isBefore(blockEndTime)) {
                throw new CustomException(ReservationErrorCode.NO_SHOW_LIMIT_EXCEEDED);
            }

            // No Show 기간이 지났다면 상태 업데이트
            noShowReservations.forEach(reservation -> reservation.setState(Reservation.ReservationState.PROCESSED));
            reservationRepository.saveAll(noShowReservations);
        }
    }

    /**
     * Room의 운영 시간 검증
     */
    private void validateRoomOperationTime(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
//        todo : 로직 작성

        Room room = partitionQueryService.getPartitionById(roomPartitionId).getRoom();
        LocalDate reservationDate = startDateTime.atZone(ZoneOffset.UTC).toLocalDate();

        RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, reservationDate)
                .orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND));

        Instant operationStartTime = DateTimeUtil.convertKstToUtc(reservationDate.atTime(schedule.getRoomOperationPolicy().getOperationStartTime()))
                .atZone(ZoneOffset.UTC).toInstant();
        Instant operationEndTime = DateTimeUtil.convertKstToUtc(reservationDate.atTime(schedule.getRoomOperationPolicy().getOperationEndTime()))
                .atZone(ZoneOffset.UTC).toInstant();

        if (startDateTime.isBefore(operationStartTime) || endDateTime.isAfter(operationEndTime)) {
            throw new CustomException(PolicyErrorCode.OPERATION_CLOSED);
        }
    }

    /**
     * Room의 예약 시간 초과 검증
     */
    private void validateMaxReservationTime(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
//        todo : 로직 작성
//        예약시 시간이 정책의 정해진 한도를 초과하는지?
        Room room = partitionQueryService.getPartitionById(roomPartitionId).getRoom();
        LocalDate reservationDate = startDateTime.atZone(ZoneOffset.UTC).toLocalDate();

        RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, reservationDate)
                .orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND));

        long reservationDuration = Duration.between(startDateTime, endDateTime).toMinutes();
        if (reservationDuration > schedule.getRoomOperationPolicy().getEachMaxMinute()) {
            throw new CustomException(ReservationErrorCode.EXCEEDING_MAX_RESERVATION_TIME);
        }
    }

    /**
     * 현재 예약 가능한 수 확인
     */
    private void validateCurrentReservations(Long userId) {
        long currentReservationCount = reservationRepository.countCurrentReservationsByUserId(userId);
        if (currentReservationCount >= reservationLimit) {
            throw new CustomException(ReservationErrorCode.TOO_MANY_CURRENT_RESERVATIONS);
        }
    }

    /**
     * 당일 예약 가능한 수 확인
     */
    private void validateTodayReservations(Long userId) {
        Instant todayStart = DateTimeUtil.getInstantStartOfToday();
        Instant todayEnd = DateTimeUtil.getInstantEndOfToday();

        long todayReservationCount = reservationRepository.countTodayReservationsByUserId(userId, todayStart, todayEnd);
        if (todayReservationCount >= reservationLimitToday) {
            throw new CustomException(ReservationErrorCode.TOO_MANY_TODAY_RESERVATIONS);
        }
    }

    /**
     * 예약 겹침 여부 확인
     */
    private void validateReservationOverlap(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
        boolean isOverlapping = reservationRepository.existsOverlappingReservation(roomPartitionId, startDateTime, endDateTime);
        if (isOverlapping) {
            throw new CustomException(ReservationErrorCode.RESERVATION_OVERLAP);
        }
    }
}
