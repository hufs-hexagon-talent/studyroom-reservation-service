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
            "WHERE r.room.roomId = :roomId " +
            "AND (r.reservationStartTime < :endTime " +
            "AND r.reservationEndTime > :startTime)"
    )
    List<Reservation> findOverlappingReservations( @Param("roomId") Long roomId,
                                                  @Param("startTime") Instant startTime,
                                                  @Param("endTime") Instant endTime
    );

    Optional<List<Reservation>> findAllByUser(User user);

    Optional<Reservation> findTopByUserUserIdOrderByReservationStartTimeDesc(Long userId);

    Optional<List<Reservation>> findByUserUserIdAndRoomRoomIdInAndReservationStartTimeBetween(Long userId, List<Long> roomIds, Instant startTime, Instant endTime);

    // 특정 날짜에 특정 방들에 대한 예약 정보를 모두 조회
    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.room.roomId IN :roomIds " +
            "AND r.reservationStartTime >= :startTime " +
            "AND r.reservationEndTime < :endTime")
    List<Reservation> findByRoomRoomIdInAndReservationStartTimeBetween(
            @Param("roomIds") List<Long> roomIds,
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
            "AND r.reservationStartTime > :startTime " +
            "AND r.state = 'NOT_VISITED'")
    List<Reservation> findByUserUserIdAndReservationStartTime(
            @Param("userId") Long userId,
            @Param("startTime") Instant startTime);

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.reservationStartTime BETWEEN :startOfToday AND :endOfToday " +
            "AND r.user.userId = :userId")
    List<Reservation> findByUserIdAndReservationStartTime(
            @Param("userId") Long userId,
            @Param("startOfToday") Instant startOfToday,
            @Param("endOfToday") Instant endOfToday);
}
