package com.test.studyroomreservationsystem.dao.impl;

import com.test.studyroomreservationsystem.dao.ReservationDao;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public class ReservationDaoImpl implements ReservationDao {
    private final ReservationRepository reservationRepository;

    public ReservationDaoImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    @Override
    public Optional<Reservation> findRecentByUserId(Long userId) {
        return reservationRepository.findTopByUserUserIdOrderByReservationStartTimeDesc(userId);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<List<Reservation>> findAllByUser(User user) {
        return reservationRepository.findAllByUser(user);
    }

    @Override
    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public List<Reservation> findOverlappingReservations(Long roomId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        return reservationRepository.findOverlappingReservations(roomId, startDateTime, endDateTime);
    }

}
