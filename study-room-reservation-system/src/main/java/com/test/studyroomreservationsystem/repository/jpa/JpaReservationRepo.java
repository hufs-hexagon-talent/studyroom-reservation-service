package com.test.studyroomreservationsystem.repository.jpa;

import com.test.studyroomreservationsystem.domain.Reservation;
import com.test.studyroomreservationsystem.repository.ReservationRepository;
import com.test.studyroomreservationsystem.repository.ReservationUpdateDto;

import java.util.List;
import java.util.Optional;

public class JpaReservationRepo implements ReservationRepository {

    private static long sequence = 0L; //static
    @Override
    public Reservation save(Reservation reservation) {
        reservation.setReservationId(++sequence);
//        put(reservation.getReservationId(),reservation)
        return reservation;
    }

    @Override
    public void update(Long reservationId, ReservationUpdateDto updateParam) {
        Reservation findedReservation = findByReservationId(reservationId).orElseThrow();
        findedReservation.setState(updateParam.getState());

    }

    @Override
    public Optional<Reservation> findByReservationId(Long reservationId) {
        return Optional.empty();
    }


    @Override
    public void deleteByReservationId(Long reservationId) {
        Reservation findedReservation = findByReservationId(reservationId).orElseThrow();
        findedReservation.setState(null);
    }
//    @Override
//    public List<Reservation> findAll() {
//        return null;
//    }

}
