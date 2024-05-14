package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.reservation.RequestReservationDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationStateDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(RequestReservationDto requestReservationDto, User user);
    Reservation findReservationById(Long reservationId);
    List<Reservation> findAllReservation();
    List<Reservation> findAllReservationByUser(Long userId);
    Reservation updateReservation(Long reservationId , RequestReservationDto requestReservationDto);
    Reservation updateTimeReservation(Long reservationId , ReservationTimeDto timeDto);
    Reservation updateStateReservation(Long reservationId , ReservationStateDto stateDto);
    Reservation updateRoomReservation(Long reservationId , ReservationRoomDto roomDto);
    void deleteReservationById(Long reservationId);
//    List<Reservation> findReservationsByDate(LocalDateTime dateTime);

    default RequestReservationDto dtoFrom(Reservation reservation) {
        return RequestReservationDto.builder()
                .roomId(reservation.getRoom().getRoomId())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .state(reservation.getState())
                .build();
    }
}
