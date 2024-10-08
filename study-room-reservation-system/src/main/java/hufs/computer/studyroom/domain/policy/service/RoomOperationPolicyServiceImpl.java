package hufs.computer.studyroom.domain.policy.service;

import hufs.computer.studyroom.common.error.code.PolicyErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.error.exception.todo.reservation.OperationClosedException;
import hufs.computer.studyroom.common.util.DateTimeUtil;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.repository.RoomOperationPolicyRepository;
import hufs.computer.studyroom.domain.policy.dto.RoomOperationPolicyDto;
import hufs.computer.studyroom.domain.policy.dto.RoomOperationPolicyUpdateDto;
import hufs.computer.studyroom.common.error.exception.todo.notfound.PolicyNotFoundException;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomOperationPolicyServiceImpl implements RoomOperationPolicyService {

    private final RoomOperationPolicyRepository policyRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;

    @Override
    public RoomOperationPolicy createPolicy(RoomOperationPolicyDto policyDto) {
        RoomOperationPolicy policyEntity = policyDto.toEntity();
        return policyRepository.save(policyEntity);
    }


    @Override
    public RoomOperationPolicy findPolicyById(Long policyId) {
        return policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));
    }

    @Override
    public List<RoomOperationPolicy> findAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public RoomOperationPolicy updatePolicy(Long policyId, RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy policy = findPolicyById(policyId);
        if (policyDto.getOperationStartTime() != null) {
            policy.setOperationStartTime(policyDto.getOperationStartTime());
        }
        if (policyDto.getOperationEndTime() != null) {
            policy.setOperationEndTime(policyDto.getOperationEndTime());
        }
        if (policyDto.getEachMaxMinute() != null) {
            policy.setEachMaxMinute(policyDto.getEachMaxMinute());
        }

        return policyRepository.save(policy);
    }

    @Override
    public void deletePolicy(Long policyId) {
        findPolicyById(policyId); // 찾아보고 없으면 예외처리
        policyRepository.deleteById(policyId);
    }

    @Override
    public void validateRoomOperation(Room room, Instant reservationStartTime, Instant reservationEndTime) {

        LocalDate date = reservationStartTime.atZone(ZoneOffset.UTC).toLocalDate();

        // 룸과 날짜로 정책 찾기
        RoomOperationPolicySchedule schedule
                = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date).orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND)); // 운영이 하지 않음 (운영 정책 없음)
//               () -> new RoomPolicyNotFoundException(room, date)
        RoomOperationPolicy roomOperationPolicy = schedule.getRoomOperationPolicy();


        Instant operationStartTime
                = DateTimeUtil.convertKstToUtc(date.atTime(roomOperationPolicy.getOperationStartTime())).atZone(ZoneOffset.UTC).toInstant();

        Instant operationEndTime
                = DateTimeUtil.convertKstToUtc(date.atTime(roomOperationPolicy.getOperationEndTime())).atZone(ZoneOffset.UTC).toInstant();

        validateReservationTime(operationStartTime, operationEndTime, reservationStartTime, reservationEndTime);
//        boolean operationBefore = reservationStartTime.isBefore(operationStartTime);
//        log.info("reservationstartTime < operationStartTime 인지 {}", operationBefore);
//
//        boolean operationAfter = reservationEndTime.isAfter(operationEndTime);
//        log.info("operationEndTime < reservationEndTime 인지 {}", operationAfter);
//
//        if (operationAfter || operationBefore) {
//            throw new OperationClosedException(
//                    room,
//                    operationStartTime, operationEndTime,
//                    reservationStartTime, reservationEndTime);
//        }

    }
    /**
     * 예약 시간이 운영 시간 내에 있는지 확인하고 예외 처리
     */
    private void validateReservationTime(Instant operationStartTime, Instant operationEndTime,
                                         Instant reservationStartTime, Instant reservationEndTime) {
        if (reservationStartTime.isBefore(operationStartTime) || reservationEndTime.isAfter(operationEndTime)) {
            throw new CustomException(PolicyErrorCode.OPERATION_CLOSED);
        }
    }

}
