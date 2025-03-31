package hufs.computer.studyroom.domain.reservation.repository;

import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.repository.projection.PartitionUsageStats;
import hufs.computer.studyroom.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공

    /**
     * 특정 RoomPartition에 대해, 주어진 시간대(startTime ~ endTime)와 겹치는 예약 목록 조회
     */
    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.roomPartition.roomPartitionId = :roomPartitionId " +
            "AND r.reservationStartTime < :endTime " +
            "AND r.reservationEndTime > :startTime ")
    List<Reservation> findOverlappingReservations(
            @Param("roomPartitionId") Long roomPartitionId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    /**
     * 검증을 위한 예약 조회 (Native Query)
     */
    // 검증을 위한 예약 가져오기 , userId , partitionIDs, 현재 시간, 현재 state 가 NOT_VISITED
    @Query(value =
            "SELECT * FROM reservation r " +
                    "WHERE r.user_id = :userId " +
                    "AND r.room_partition_id IN :roomPartitionIds " +
                    "AND r.state = :notVisitedState " +
                    "AND :nowTime BETWEEN (r.reservation_start_time - INTERVAL :allowedStartMinute MINUTE) AND r.reservation_end_time",
            nativeQuery = true)
    List<Reservation> findValidReservations(
            @Param("userId") Long userId,
            @Param("roomPartitionIds") List<Long> roomPartitionIds,
            @Param("nowTime") Instant nowTime,
            @Param("allowedStartMinute") Long allowedStartMinute,
            @Param("notVisitedState") String notVisitedState);


    /**
     * 특정 날짜 구간(startTime ~ endTime) 동안 특정 roomPartitionIds에 속하는 예약 조회
     */
    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.roomPartition.roomPartitionId IN :roomPartitionIds " +
            "AND r.reservationStartTime >= :startTime " +
            "AND r.reservationEndTime < :endTime")
    List<Reservation> findByRoomPartitionRoomPartitionIdInAndReservationStartTimeBetween(
            @Param("roomPartitionIds") List<Long> roomPartitionIds,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    /**
     * 사용자의 No Show 상태인 예약 조회
     * 예약이 종료시점(CURRENT_TIMESTAMP)보다 이전인데도 NOT_VISITED 상태인 것
     */
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.user.userId = :userId " +
            "AND r.state = 'NOT_VISITED' " +
            "AND r.reservationEndTime < CURRENT_TIMESTAMP")
    List<Reservation> findNoShowReservationsByUserId(
            @Param("userId") Long userId);

    /**
     * 사용자가 현재 'NOT_VISITED' 상태인 예약들 중 종료되지 않은 것 조회
     */
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.user.userId = :userId " +
            "AND r.state = 'NOT_VISITED' " +
            "AND r.reservationEndTime >= CURRENT_TIMESTAMP")
    List<Reservation> findCurrentReservationsByUserId(
            @Param("userId") Long userId);

    /**
     * 특정 RoomPartition에 대해, 주어진 시간대와 겹치는 예약이 존재하는지 여부
     * -> count가 0보다 큰지 체크
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.roomPartition.roomPartitionId = :roomPartitionId " +
            "AND r.reservationStartTime < :endDateTime " +
            "AND r.reservationEndTime > :startDateTime")
    boolean existsOverlappingReservation(
            @Param("roomPartitionId") Long roomPartitionId,
            @Param("startDateTime") Instant startDateTime,
            @Param("endDateTime") Instant endDateTime);

    /**
     * 사용자가 체크인해야 하는 NOT_VISITED 상태 예약 중 가장 최근것(페이지네이션)
     */
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.user.userId = :userId " +
            "AND r.state = 'NOT_VISITED' " +
            "AND r.reservationEndTime >= CURRENT_TIMESTAMP " +
            "ORDER BY r.createAt DESC")
    List<Reservation> findRecentValidReservationByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );

    /**
     * 해당 유저의 모든 reservation 로그 찾기
     */
    @Query("SELECT r FROM Reservation r " +
    "WHERE r.user.userId = :userId")
    List<Reservation> findAllByUserUserId(Long userId);

    /**
     * 특정 유저가 오늘 생성한 예약( createAt 기준 ) 갯수 조회
     */
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.user.userId = :userId " +
            "AND r.createAt BETWEEN :todayStart AND :todayEnd")
    long countTodayReservationsByUserId(
            @Param("userId") Long userId,
            @Param("todayStart") Instant todayStart,
            @Param("todayEnd") Instant todayEnd);

    /**
     * 특정 구간(start ~ end)에 생성된 예약 총 갯수
     */
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.createAt BETWEEN :start AND :end")
    long countReservationsWithinRange(
            @Param("start") Instant start,
            @Param("end") Instant end);

    /**
     * 특정 시점(end) 이전까지 생성된 예약 총 갯수
     */
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.createAt <= :end")
    long countReservationsBefore(
            @Param("end") Instant end);

    @Query(value = """
    SELECT r.room_partition_id AS partitionId,
           COUNT(*) AS reservationCount,
           SUM(TIMESTAMPDIFF(MINUTE, r.reservation_start_time, r.reservation_end_time)) AS totalReservationMinutes
    FROM reservation r
    WHERE r.create_at BETWEEN :startTime AND :endTime
    GROUP BY r.room_partition_id
    """, nativeQuery = true)
    List<PartitionUsageStats> findPartitionUsageStats(
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );
}
