package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.common.error.code.*;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.DateTimeUtils;
import hufs.computer.studyroom.domain.partition.service.PartitionQueryService;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

import static hufs.computer.studyroom.common.util.DateTimeUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationValidationService {
    @Value("${spring.service.reservationLimitCurrent}") private int reservationLimitCurrent;
    @Value("${spring.service.reservationLimitToday}") private int reservationLimitToday;

    private final ReservationRepository reservationRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final PartitionQueryService partitionQueryService;


    public void validateRoomAvailability(Long userId, Long roomPartitionId, Instant startDateTime, Instant endDateTime) {

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
     * Room의 운영 시간 검증
     */
    private void validateRoomOperationTime(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {


        Room room = partitionQueryService.getPartitionById(roomPartitionId).getRoom();
//       utc -> kst -> data
        LocalDate kstDate = DateTimeUtils.convertUtcToKst(startDateTime).toLocalDate();

        RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(room, kstDate)
                .orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND));

        LocalTime operationStartKst = schedule.getRoomOperationPolicy().getOperationStartTime();
        LocalTime operationEndKst = schedule.getRoomOperationPolicy().getOperationEndTime();

        Instant operationStartTime = convertKstToUtc(kstDate.atTime(operationStartKst));
        Instant operationEndTime = convertKstToUtc(kstDate.atTime(operationEndKst));

        if (startDateTime.isBefore(operationStartTime) || endDateTime.isAfter(operationEndTime)) {
            throw new CustomException(PolicyErrorCode.OPERATION_CLOSED);
        }
    }

    /**
     * Room의 예약 시간 초과 검증
     */
    private void validateMaxReservationTime(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {

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
        int currentReservationCount = reservationRepository.findCurrentReservationsByUserId(userId).size();
        if (currentReservationCount >= reservationLimitCurrent) {
            throw new CustomException(ReservationErrorCode.TOO_MANY_CURRENT_RESERVATIONS);
        }
    }

    /**
     * 당일 예약 가능한 수 확인
     */
    private void validateTodayReservations(Long userId) {
        Instant todayStart = getInstantStartOfToday();
        Instant todayEnd = getInstantEndOfToday();

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
