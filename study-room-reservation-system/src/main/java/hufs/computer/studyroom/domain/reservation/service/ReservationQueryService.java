package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.common.error.code.*;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.DateTimeUtil;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.reservation.dto.response.*;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.reservation.mapper.ReservationMapper;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.entity.User.ServiceRole;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static hufs.computer.studyroom.common.util.DateTimeUtil.convertKstToUtc;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomPartitionRepository partitionRepository;
    private final ReservationMapper reservationMapper;


    // 해당 유저의 모든 reservation 로그 찾기
//    todo : RESERVATION_HISTORY_NOT_FOUND 예외로 처리 할 지, 빈배열 반환을 할지?
    public ReservationInfoResponses findAllReservationByUser(CustomUserDetails currentUser) {
        Long userId = currentUser.getUser().getUserId();

        List<Reservation> reservations = reservationRepository.findAllByUserUserId(userId)
                .orElse(Collections.emptyList());
//              .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_HISTORY_NOT_FOUND));

        return reservationMapper.toInfoResponses(reservations);
    }

//    todo : RESERVATION_HISTORY_NOT_FOUND 예외로 처리 할 지, 빈배열 반환을 할지?
    public ReservationInfoResponses findAllReservationBySerial(String serial, CustomUserDetails currentUser) {
        User admin = currentUser.getUser();
        if (admin.getServiceRole() != null && admin.getServiceRole() != ServiceRole.ADMIN) {
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }

        User foundUser = userRepository.findBySerial(serial).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        Long userId = foundUser.getUserId();
        List<Reservation> reservations = reservationRepository.findAllByUserUserId(userId)
                .orElse(Collections.emptyList());
//              .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_HISTORY_NOT_FOUND));

        return reservationMapper.toInfoResponses(reservations);
    }

    /**
     * 사용자 NoShow 예약 목록 조회
     * @param currentUser 현재 인증된 사용자
     * @return UserNoShowCntResponse - 노쇼 예약 횟수와 예약 목록
     */
    public UserNoShowCntResponse getNoShowReservations(CustomUserDetails currentUser) {
        Long userId = currentUser.getUser().getUserId();
        List<Reservation> noShowReservations = reservationRepository.findNoShowReservationsByUserId(userId);

        return reservationMapper.toUserNoShowCntResponse(noShowReservations.size(), noShowReservations);
    }

//todo : createAt (예약생성 시간)기준으로 가져올지 vs reservationStartTime ( 예약 시작 시간 ) 기준으로 가져올지
//    reservationStartTime ( 예약 시작 시간 ) 기준
//    todo : RESERVATION_HISTORY_NOT_FOUND 예외로 처리 할 지, 빈배열 반환을 할지?
    public ReservationInfoResponse getRecentReservationByUser(CustomUserDetails currentUser) {
        Long userId = currentUser.getUser().getUserId();
        Reservation reservation = reservationRepository.findTopByUserUserIdOrderByReservationStartTimeDesc(userId)
                .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_HISTORY_NOT_FOUND));
        return reservationMapper.toInfoResponse(reservation);
    }

    /**
     * 주어진 날짜의 모든 파티션들의 예약 상태 조회
     * @param date 조회할 날짜
     * @param departmentId 룸을 관리하는 부서 ID
     * @return AllPartitionsReservationStatusResponse - 각 파티션의 예약 현황 정보
     */
    public AllPartitionsReservationStatusResponse getPartitionReservationsByDepartmentAndDate(Long departmentId, LocalDate date) {

//       departmentId 에 속하는 룸들의 Partitions 들을 찾음
        List<RoomPartition> partitions = partitionRepository.findAllByRoomDepartmentDepartmentId(departmentId);

        List<PartitionReservationStatus> partitionReservationStatuses = partitions.stream()
                .map(partition -> getEachPartitionReservationState(partition, date))
                .filter(info -> info != null)  // 운영 정책이 없는 파티션들은 null로 처리
                .collect(Collectors.toList());

        return reservationMapper.toAllPartitionsReservationStatusResponse(partitionReservationStatuses);
    }

    private PartitionReservationStatus getEachPartitionReservationState(RoomPartition partition , LocalDate date) {
        RoomOperationPolicySchedule schedule = scheduleRepository.findByRoomAndPolicyApplicationDate(partition.getRoom(), date).orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        RoomOperationPolicy policy = schedule.getRoomOperationPolicy();
        // 운영 시작 및 종료 시간을 계산
        Instant operationStartTime = convertKstToUtc(date.atTime(policy.getOperationStartTime())).atZone(ZoneOffset.UTC).toInstant();
        Instant operationEndTime = convertKstToUtc(date.atTime(policy.getOperationEndTime())).atZone(ZoneOffset.UTC).toInstant();

        // 운영 시간 동안의 예약들 조회
        List<Reservation> reservations = reservationRepository.findOverlappingReservations(partition.getRoomPartitionId(), operationStartTime, operationEndTime);


        List<ReservationTimeRange> reservationTimeRanges = reservations.stream()
                .map(reservationMapper::toTimeRange)
                .collect(Collectors.toList());


        return reservationMapper.toPartitionReservationStatus(partition, policy, reservationTimeRanges);
    }


    /**
     * 주어진 날짜의 모든 파티션들의 예약 상태 조회
     * @param partitionIds 조회할 파티션 ID 목록
     * @param date 조회할 날짜
     * @return ReservationInfoResponses - ReservationInfoResponse 들의 목록
     */

    public ReservationInfoResponses getReservationsByPartitionsAndDate(List<Long> partitionIds, LocalDate date) {
        Instant startTime = DateTimeUtil.getInstantStartOfDate(date);
        Instant endTime = DateTimeUtil.getInstantEndOfDate(date);

        List<Reservation> reservations
                = reservationRepository.findByRoomPartitionRoomPartitionIdInAndReservationStartTimeBetween(partitionIds, startTime, endTime);

        return reservationMapper.toInfoResponses(reservations);
    }

    /* Helper Method
    * */
    public List<Reservation> findValidReservations(Long userId, List<Long> roomPartitionIds, Instant nowTime, Long allowedStartMinute) {
        String notVisitedState = ReservationState.NOT_VISITED.toString();
        return reservationRepository.findValidReservations(userId, roomPartitionIds, nowTime, allowedStartMinute,notVisitedState)
                .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }
    public boolean existByReservationId(Long reservationId) {return reservationRepository.existsById(reservationId);}

}