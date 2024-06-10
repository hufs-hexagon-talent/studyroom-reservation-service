package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long reservationId);
    Optional<Reservation> findRecentByUserId(Long userId);
    List<Reservation> findAll();
    Optional<List<Reservation>> findAllByUser(User user);
    void deleteById(Long reservationId);
    List<Reservation> findOverlappingReservations(Long roomId, Instant startDateTime, Instant endDateTime);
    Optional<List<Reservation>> findByUserIdAndRoomIdsAndStartTimeBetween(Long userId, List<Long> roomIds, Instant startDateTime, Instant endDateTime);
    List<Reservation> findByRoomIdsAndStartTimeBetween(List<Long> roomIds, Instant startTime, Instant endTime);
    List<Reservation> countNoShowsByUserIdAndPeriod(Long userId, Instant startDateTime, Instant endDateTime);
    List<Reservation> getNotVisitedReservationsAfterNow(Long userId, Instant startTime);
}
