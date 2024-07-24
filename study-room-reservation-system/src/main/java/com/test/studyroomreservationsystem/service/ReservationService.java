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
    void deleteReservationBySelf(Long reservationId, CustomUserDetails currentUser);
    void deleteReservationByAdmin(Long reservationId, CustomUserDetails currentUser);
    List<PartitionsReservationResponseDto> getReservationsByAllPartitionsAndDate(LocalDate date);
    SpecificRoomsReservationsDto getReservationsByPartitionsAndDate(List<Long> partitionIds, LocalDate date);
    List<Reservation> findValidReservations(Long userId, List<Long> roomPartitionIds, Instant nowTime, Long allowedStartMinute);
    List<Reservation> getReservationsByUserIdAndToday(Long userId);
    List<Reservation> getNotVisitedReservationsAfterNow(Long userId);
    List<Reservation> countNoShowsByUserIdAndPeriod(Long userId);


    ReservationInfoResponseDto responseDtoFrom(Reservation reservation);

}
