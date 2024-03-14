package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.ReservationCreateDto;
import com.test.studyroomreservationsystem.dto.ReservationUpdateDto;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationCreateDto reservationDto);
    Reservation findReservationById(Long reservationId);
    List<Reservation> findAllReservation();
    Reservation updateReservation(Long reservationId , ReservationUpdateDto reservationUpdateDto);
    void deleteReservationById(Long reservationId);

}
