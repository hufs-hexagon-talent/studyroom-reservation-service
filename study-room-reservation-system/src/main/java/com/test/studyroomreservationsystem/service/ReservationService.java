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
    List<Reservation> getCurrentReservations(Long userId);
    List<Reservation> getNoShowReservations(Long userId);


    default ReservationInfoResponseDto responseDtoFrom(Reservation reservation) {
        return ReservationInfoResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUser().getUserId())
                .roomId(reservation.getRoomPartition().getRoom().getRoomId())
                .roomName(reservation.getRoomPartition().getRoom().getRoomName())
                .roomPartitionId(reservation.getRoomPartition().getRoomPartitionId())
                .partitionNumber(reservation.getRoomPartition().getPartitionNumber())
                .startDateTime(reservation.getReservationStartTime())
                .endDateTime(reservation.getReservationEndTime())
                .reservationState(reservation.getState())
                .build();


    }
    default List<SpecificRoomsReservationsDto.RoomReservation> responseDtoFrom(List<Reservation> reservations) {
        List<SpecificRoomsReservationsDto.RoomReservation> roomReservations;
        roomReservations = reservations.stream()
                .map(reservation -> new SpecificRoomsReservationsDto.RoomReservation(
                        reservation.getReservationId(),
                        reservation.getRoomPartition().getRoom().getRoomId(),
                        reservation.getRoomPartition().getRoom().getRoomName(),
                        reservation.getRoomPartition().getRoomPartitionId(),
                        reservation.getRoomPartition().getPartitionNumber(),
                        reservation.getUser().getUserId(),
                        reservation.getUser().getName(),
                        reservation.getState(),
                        reservation.getReservationStartTime(),
                        reservation.getReservationEndTime()))
                .toList();

        return roomReservations;
    }

}
