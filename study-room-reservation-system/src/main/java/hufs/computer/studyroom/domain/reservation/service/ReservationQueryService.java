package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.common.error.code.*;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.DateTimeUtils;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.reservation.dto.excel.ReservationExportExcelDto;
import hufs.computer.studyroom.domain.reservation.dto.response.*;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.reservation.mapper.ReservationMapper;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.reservation.repository.projection.PartitionUsageStats;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static hufs.computer.studyroom.common.util.DateTimeUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationQueryService {
    @Value("${spring.service.noShowBlockMonth}") private Long noShowBlockMonth;
    @Value("${spring.service.noShowLimit}") private int noShowLimit;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomPartitionRepository partitionRepository;
    private final ReservationMapper reservationMapper;
    private final RoomRepository roomRepository;


    // 해당 유저의 모든 reservation 로그 찾기
//    todo : RESERVATION_HISTORY_NOT_FOUND 예외로 처리 할 지, 빈배열 반환을 할지?

    public ReservationInfoResponses findAllReservationByUser(Long userId) {
        List<Reservation> reservations = reservationRepository.findAllByUserUserId(userId);
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

    /**
     * @Param null
     * @return todo : DTO (네이밍 생각하기)
     */

    public BlockedUserNoShowResponses getBlockedUserReservation() {
//        1. 밴된 유저들 찾기
        List<User> blockedUsers = userRepository.getBlockedUsers();

//        2. 해당 유저에 대해서
        List<UserNoShowCntResponse> usersNoShowCntResponse = blockedUsers.stream()
                .map(blockedUser ->
                        {
                            Long blockedUserId = blockedUser.getUserId();
                            List<Reservation> noShowReservations = reservationRepository.findNoShowReservationsByUserId(blockedUserId);
                            return reservationMapper.toUserNoShowCntResponse(noShowReservations.size(), noShowReservations);
                        }
                ).collect(Collectors.toList());
        return reservationMapper.toUserNoShowCntResponses(usersNoShowCntResponse);

    }

    /**
     * 사용자 NoShow 예약 목록 조회
     * @param currentUser 현재 인증된 사용자
     * @return ReservationInfoResponses - 가장 최신에 생성된 + 현재 예약 종료 전까지 + NOT_VISITED 인 예약
     */
    public ReservationInfoResponses getRecentReservationByUser(CustomUserDetails currentUser) {
        Long userId = currentUser.getUser().getUserId();
        Pageable pageable = PageRequest.of(0, 1);
        List<Reservation> foundReservations = reservationRepository.findRecentValidReservationByUserId(userId, pageable);

        return reservationMapper.toInfoResponses(foundReservations);
    }

    /**
     * 주어진 날짜의 모든 파티션들의 예약 상태 조회
     * @param date 조회할 날짜
     * @param departmentId 룸을 관리하는 부서 ID
     * @return AllPartitionsReservationStatusResponse - 각 파티션의 예약 현황 정보
     */
    public AllPartitionsReservationStatusResponse getPartitionReservationsByDepartmentAndDate(Long departmentId, LocalDate date) {

//       departmentId 에 속하는 룸들의 Partitions 들을 찾음
        List<Room> roomsOfDepartment = roomRepository.findAllByDepartmentDepartmentId(departmentId);
        List<RoomPartition> partitions = partitionRepository.findByRoomIn(roomsOfDepartment);

        List<PartitionReservationStatus> partitionReservationStatuses = partitions.stream()
                .map(partition -> getEachPartitionReservationState(partition, date))
                .filter(info -> info != null)  // 운영 정책이 없는 파티션들은 null로 처리
                .collect(Collectors.toList());

        return reservationMapper.toAllPartitionsReservationStatusResponse(partitionReservationStatuses);
    }

    private PartitionReservationStatus getEachPartitionReservationState(RoomPartition partition , LocalDate dateKst) {
        // 운영 정책 일정이 없는 경우 null 반환
        Optional<RoomOperationPolicySchedule> optionalSchedule = scheduleRepository.findByRoomAndPolicyApplicationDate(partition.getRoom(), dateKst);
        if (optionalSchedule.isEmpty()) {
            return null; // 운영 정책이 없으면 null 반환
        }

        RoomOperationPolicySchedule schedule = optionalSchedule.get();
        RoomOperationPolicy policy = schedule.getRoomOperationPolicy();
        // 운영 시작 및 종료 시간을 계산

        LocalTime operationStartKst = policy.getOperationStartTime();
        LocalTime operationEndKst = policy.getOperationEndTime();

        Instant operationStartTime = convertKstToUtc(dateKst.atTime(operationStartKst));
        Instant operationEndTime = convertKstToUtc(dateKst.atTime(operationEndKst));

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
        Instant startTime = DateTimeUtils.getInstantStartOfDate(date);
        Instant endTime = DateTimeUtils.getInstantEndOfDate(date);

        List<Reservation> reservations
                = reservationRepository.findByRoomPartitionRoomPartitionIdInAndReservationStartTimeBetween(partitionIds, startTime, endTime);

        return reservationMapper.toInfoResponses(reservations);
    }

    /* Helper Method
    * */

    /**
     * 사용자의 NO_SHOW (유효 기간 + NOT_VISITED)
     */
    public List<Reservation> getNoShowReservationsByUserId(Long userId) {
        return reservationRepository.findNoShowReservationsByUserId(userId);
    }

    /**
     * 사용자에 대한 No-Show 블락 시작 시간을 계산
     */
    private Instant getLatestNoShowStartTime(Long userId) {
        List<Reservation> noShowReservations = reservationRepository.findNoShowReservationsByUserId(userId);
        return noShowReservations.stream()
                .map(Reservation::getCreateAt)
                .max(Instant::compareTo)
                .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    /**
     * 사용자에 대한 No-Show 블락 시작 시간을 반환
     */
    public LocalDate getBlockedStartTime(Long userId) {
        return getDateOfInstant(getLatestNoShowStartTime(userId));
    }

    /**
     * 사용자에 대한 No-Show 블락 종료 시간을 계산
     */
    private Instant calculateNoShowBlockEndTime(Long userId) {
        Instant latestNoShowTime = getLatestNoShowStartTime(userId);
        return DateTimeUtils.getInstantMonthAfter(latestNoShowTime, noShowBlockMonth);
    }

    /**
     * 사용자에 대한 No-Show 블락 종료 시간을 반환
     */
    public LocalDate getBlockedEndTime(Long userId) {
        return getDateOfInstant(calculateNoShowBlockEndTime(userId));
    }

    /*
    * */
    public List<Reservation> findValidReservations(Long userId, List<Long> roomPartitionIds, Instant nowTime, Long allowedStartMinute) {
        String notVisitedState = ReservationState.NOT_VISITED.toString();
        return reservationRepository.findValidReservations(userId, roomPartitionIds, nowTime, allowedStartMinute,notVisitedState);
    }

    /**
     * 사용자의 예약이 No-Show 제한을 초과했는지?
     */
    public boolean isNoShowOverLimit(Long userId){
        List<Reservation> noShowReservations = reservationRepository.findNoShowReservationsByUserId(userId);

        boolean isOver = noShowReservations.size() > noShowLimit;
        return isOver;
    }

    /**
     * BLOCKED 의 유효기한이 만료되었는지?
     */
    public boolean isBlockExpired(Long userId){

        Instant blockEndTime = calculateNoShowBlockEndTime(userId);
        boolean isExpired = blockEndTime.isBefore(Instant.now());
        return isExpired;
    }

    /**
     * 기간별 통계를 조회
     */
    public ReservationStaticResponse getReservationStaticsByDate(LocalDate date){
//      모든 방에 대해서
        Instant todayStart = getInstantStartOfToday(date);
        Instant todayEnd = getInstantEndOfToday(date);
        Instant beforeWeekStart = getInstantDayBefore(todayStart,7L);
        Instant beforeMonthStart = getInstantMonthBefore(todayStart,1L);

        // day
        List<PartitionUsageStats> dayStats = reservationRepository.findPartitionUsageStats(todayStart, todayEnd);
        List<PartitionUsageStatsResponse> partitionStatsToday =
                reservationMapper.toPartitionUsageStatsResponses(dayStats);

        // week
        List<PartitionUsageStats> weekStats = reservationRepository.findPartitionUsageStats(beforeWeekStart, todayEnd);
        List<PartitionUsageStatsResponse> partitionStatsWeekly =
                reservationMapper.toPartitionUsageStatsResponses(weekStats);

        // month
        List<PartitionUsageStats> monthStats = reservationRepository.findPartitionUsageStats(beforeMonthStart, todayEnd);
        List<PartitionUsageStatsResponse> partitionStatsMonthly =
                reservationMapper.toPartitionUsageStatsResponses(monthStats);

        // total
        List<PartitionUsageStats> totalStats = reservationRepository.findPartitionUsageStats(Instant.EPOCH, todayEnd);
        List<PartitionUsageStatsResponse> partitionStatsTotal =
                reservationMapper.toPartitionUsageStatsResponses(totalStats);

        return reservationMapper.toReservationStatic(
                partitionStatsToday,
                partitionStatsWeekly,
                partitionStatsMonthly,
                partitionStatsTotal
        );
    }

    public List<ReservationExportExcelDto> getExcelDTOs(Collection<ReservationState> states,
                                                    Instant startDateTime,
                                                    Instant endDateTime){
        List<Reservation> reservations =
                reservationRepository.findByFilter(states, startDateTime, endDateTime);

        return reservationMapper.toExportExcelDTOs(reservations);
    }

    // -- 헬퍼 --
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }
    public boolean existByReservationId(Long reservationId) {return reservationRepository.existsById(reservationId);}

}
