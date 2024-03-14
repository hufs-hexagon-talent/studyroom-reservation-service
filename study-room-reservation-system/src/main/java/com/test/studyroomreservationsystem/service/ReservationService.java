package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationDto reservationDto);
    Reservation findReservationById(Long reservationId);
    List<Reservation> findAllReservation();
    Reservation updateReservation(Long reservationId , ReservationDto reservationDto);
    void deleteReservationById(Long reservationId);

}
