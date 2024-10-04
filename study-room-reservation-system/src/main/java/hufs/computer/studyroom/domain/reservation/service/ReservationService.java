package hufs.computer.studyroom.domain.reservation.service;


import hufs.computer.reservation.dto.*;
import hufs.computer.studyroom.domain.reservation.dto.*;
import hufs.computer.studyroom.reservation.dto.*;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.user.entity.User;
import com.computer.studyroom.dto.reservation.*;
import hufs.computer.studyroom.dto.reservation.*;
import hufs.computer.studyroom.security.CustomUserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationRequestDto reservationRequestDto, User user);
    Reservation findReservationById(Long reservationId);
    Reservation findRecentReservationByUserId(Long userId);
    List<Reservation> findAllReservation();
    List<Reservation> findAllReservationByUser(Long userId);
    List<Reservation> findAllReservationByAdmin(String serial, CustomUserDetails currentUser);
    void deleteReservationBySelf(Long reservationId, CustomUserDetails currentUser);
    void deleteReservationByAdmin(Long reservationId, CustomUserDetails currentUser);

    Reservation updateReservationInfo(Long reservationId, ReservationInfoUpdateRequestDto requestDto);
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
                .createAt(reservation.getCreateAt())
                .updateAt(reservation.getUpdateAt())
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
                        reservation.getReservationEndTime(),
                        reservation.getCreateAt(),
                        reservation.getUpdateAt()))
                .toList();

        return roomReservations;
    }

}
