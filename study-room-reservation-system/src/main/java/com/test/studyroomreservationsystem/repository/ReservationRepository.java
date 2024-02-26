package com.test.studyroomreservationsystem.repository;

import com.test.studyroomreservationsystem.domain.Reservation;

//import java.util.List;
import java.util.Optional;


public interface ReservationRepository {
    Reservation save(Reservation reservation);
    void update(Long reservationId , ReservationUpdateDto updateParam);
    Optional<Reservation> findByReservationId(Long reservationId);
//    List<Reservation> findAll();
    void deleteByReservationId(Long reservationId);

}
