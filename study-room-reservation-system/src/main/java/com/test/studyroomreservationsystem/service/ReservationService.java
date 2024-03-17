package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.reservation.ReservationDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationStateDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationDto reservationDto);
    Reservation findReservationById(Long reservationId);
    List<Reservation> findAllReservation();
    Reservation updateReservation(Long reservationId , ReservationDto reservationDto);
    Reservation updateTimeReservation(Long reservationId , ReservationTimeDto timeDto);
    Reservation updateStateReservation(Long reservationId , ReservationStateDto stateDto);
    Reservation updateRoomReservation(Long reservationId , ReservationRoomDto roomDto);

    void deleteReservationById(Long reservationId);
    ReservationDto convertToDto(Reservation reservation);
}
