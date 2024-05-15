package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRequestDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationStateDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationRequestDto reservationRequestDto, User user);
    Reservation findReservationById(Long reservationId);
    Reservation findRecentReservationByUserId(Long userId);
    List<Reservation> findAllReservation();
    List<Reservation> findAllReservationByUser(Long userId);
    Reservation updateReservation(Long reservationId , ReservationRequestDto reservationRequestDto);
    Reservation updateTimeReservation(Long reservationId , ReservationTimeDto timeDto);
    Reservation updateStateReservation(Long reservationId , ReservationStateDto stateDto);
    Reservation updateRoomReservation(Long reservationId , ReservationRoomDto roomDto);
    void deleteReservationById(Long reservationId);
//    List<Reservation> findReservationsByDate(LocalDateTime dateTime);

    default ReservationRequestDto dtoFrom(Reservation reservation) {
        return ReservationRequestDto.builder()
                .roomId(reservation.getRoom().getRoomId())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .build();
    }
}
