package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long reservationId);
    List<Reservation> findAll();
    Optional<List<Reservation>> findAllByUser(User user);
    void deleteById(Long reservationId);
    List<Reservation> findOverlappingReservations(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Reservation> findAllReservationsByDate(LocalDate date);
}