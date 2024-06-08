package com.test.studyroomreservationsystem.service;


import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.reservation.*;
import com.test.studyroomreservationsystem.security.CustomUserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationRequestDto reservationRequestDto, User user);
    Reservation findReservationById(Long reservationId);
    Reservation findRecentReservationByUserId(Long userId);
    List<Reservation> findAllReservation();
    List<Reservation> findAllReservationByUser(Long userId);
    void deleteReservation(Long reservationId, CustomUserDetails currentUser);
    List<RoomsReservationResponseDto> getReservationsByAllRoomsAndDate(LocalDate date);
    SpecificRoomsReservationsDto getReservationsByRoomsAndDate(List<Long> roomIds, LocalDate date);
    List<Reservation> findByUserIdAndRoomIdAndStartTimeBetween(Long userId,List<Long> roomIds, Instant startTime, Instant endTime);
    Long countNoShowsByUserIdAndPeriod(Long userId);


    ReservationRequestDto requestDtoFrom(Reservation reservation);
    ReservationResponseDto responseDtoFrom(Reservation reservation);

}
