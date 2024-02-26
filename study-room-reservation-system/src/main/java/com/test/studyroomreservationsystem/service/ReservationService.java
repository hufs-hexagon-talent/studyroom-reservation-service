package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.Reservation;
import com.test.studyroomreservationsystem.repository.ReservationUpdateDto;

import java.util.Optional;

public interface ReservationService {
    Reservation save(Reservation reservation);
    void update(Long reservationId , ReservationUpdateDto updateParam);
    Optional<Reservation> findByReservationId(Long reservationId);
    void deleteByReservationId(Long reservationId);

    //    List<Reservation> findAll();
}
