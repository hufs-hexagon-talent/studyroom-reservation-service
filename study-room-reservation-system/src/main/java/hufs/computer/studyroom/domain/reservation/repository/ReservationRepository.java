package hufs.computer.studyroom.domain.reservation.repository;

import hufs.computer.studyroom.domain.reservation.entity.Reservation;
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

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.roomPartition.roomPartitionId = :roomPartitionId " +
            "AND (r.reservationStartTime < :endTime " +
            "AND r.reservationEndTime > :startTime)"
    )
    List<Reservation> findOverlappingReservations( @Param("roomPartitionId") Long roomPartitionId,
                                                  @Param("startTime") Instant startTime,
                                                  @Param("endTime") Instant endTime
    );

    // 검증을 위한 예약 가져오기 , userId , partitionIDs, 현재 시간, 현재 state 가 NOT_VISITED
    @Query(value =
            "SELECT * FROM reservation r WHERE r.user_id = :userId " +
            "AND r.room_partition_id IN :roomPartitionIds " +
            "AND r.state = :notVisitedState " +
            "AND :nowTime BETWEEN (r.reservation_start_time - INTERVAL :allowedStartMinute MINUTE) AND r.reservation_end_time",
            nativeQuery = true)
    Optional<List<Reservation>> findValidReservations(
            @Param("userId") Long userId,
            @Param("roomPartitionIds") List<Long> roomPartitionIds,
            @Param("nowTime") Instant nowTime,
            @Param("allowedStartMinute") Long allowedStartMinute,
            @Param("notVisitedState")String notVisitedState);

    // 특정 날짜에 특정 방들에 대한 예약 정보를 모두 조회
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


    // 사용자의 No Show 상태인 예약을 조회하는 메서드
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.user.userId = :userId AND" +
                 " r.state = 'NOT_VISITED' AND " +
                 " r.reservationEndTime < CURRENT_TIMESTAMP")
    List<Reservation> findNoShowReservationsByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Reservation r WHERE r.user.userId = :userId AND r.state = 'NOT_VISITED' AND r.reservationEndTime >= CURRENT_TIMESTAMP")
    List<Reservation> findCurrentReservationsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.userId = :userId AND r.createAt BETWEEN :todayStart AND :todayEnd")
    long countTodayReservationsByUserId(@Param("userId") Long userId, @Param("todayStart") Instant todayStart, @Param("todayEnd") Instant todayEnd);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.roomPartition.roomPartitionId = :roomPartitionId AND (r.reservationStartTime < :endDateTime AND r.reservationEndTime > :startDateTime)")
    boolean existsOverlappingReservation(@Param("roomPartitionId") Long roomPartitionId, @Param("startDateTime") Instant startDateTime, @Param("endDateTime") Instant endDateTime);

    // 사용자가 현재 체크인 해야하는 예약 조회
    @Query( " SELECT r FROM Reservation r" +
            " WHERE r.user.userId = :userId AND" +
            " r.state = 'NOT_VISITED' AND" +
            " r.reservationEndTime >= CURRENT_TIMESTAMP" +
            " ORDER BY r.createAt DESC")
    List<Reservation> findRecentValidReservationByUserId(Long userId, Pageable pageable);
// 해당 유저의 모든 reservation 로그 찾기
    Optional<List<Reservation>> findAllByUserUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.createAt BETWEEN :start AND :end")
    long countRangeReservations(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.createAt <= :end")
    long countRangeReservations( @Param("end") Instant end);
}
