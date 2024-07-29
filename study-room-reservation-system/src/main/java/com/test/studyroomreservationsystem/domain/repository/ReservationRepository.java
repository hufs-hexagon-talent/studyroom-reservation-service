package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
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
            "AND (r.reservationStartTime <= :endTime " +
            "AND r.reservationEndTime >= :startTime)"
    )
    List<Reservation> findOverlappingReservations( @Param("roomPartitionId") Long roomPartitionId,
                                                  @Param("startTime") Instant startTime,
                                                  @Param("endTime") Instant endTime
    );

    Optional<List<Reservation>> findAllByUser(User user);

    Optional<Reservation> findTopByUserUserIdOrderByReservationStartTimeDesc(Long userId);

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

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.user.userId = :userId " +
            "AND r.state = 'NOT_VISITED' " +
            "AND r.reservationStartTime BETWEEN :startTime AND :endTime")
    List<Reservation> countNoShowsByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.user.userId = :userId " +
            "AND r.state = 'NOT_VISITED'" +
            "AND r.reservationEndTime > :nowTime "
            )
    List<Reservation> findCurrentReservations(
            @Param("userId") Long userId,
            @Param("nowTime") Instant nowTime);

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.reservationStartTime BETWEEN :startOfToday AND :endOfToday " +
            "AND r.user.userId = :userId")
    List<Reservation> findByUserIdAndReservationStartTime(
            @Param("userId") Long userId,
            @Param("startOfToday") Instant startOfToday,
            @Param("endOfToday") Instant endOfToday);
}
