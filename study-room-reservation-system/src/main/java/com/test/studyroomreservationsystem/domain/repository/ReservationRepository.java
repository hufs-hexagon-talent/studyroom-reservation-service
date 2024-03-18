package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.room.roomId = :roomId AND (r.startDateTime < :endTime AND r.endDateTime > :startTime)"
    )
    List<Reservation> findOverlappingReservations( @Param("roomId") Long roomId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime
    );

    Optional<List<Reservation>> findAllByUser(User user);

}
