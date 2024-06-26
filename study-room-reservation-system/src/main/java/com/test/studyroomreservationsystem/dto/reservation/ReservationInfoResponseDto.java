package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter

public class ReservationInfoResponseDto {
    private Long reservationId;
    private Long userId;
    private Long roomId;
    private String roomName;
    private Instant startDateTime;
    private Instant endDateTime;
    private ReservationState reservationState;

    @Builder
    public ReservationInfoResponseDto(Long reservationId,Long userId, Long roomId, String roomName, Instant startDateTime, Instant endDateTime, ReservationState reservationState) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reservationState = reservationState;
    }
    public Reservation toEntity(User user, Room room) {
        return Reservation.builder()
                .reservationId(reservationId)
                .user(user)
                .room(room)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(reservationState)
                .build();
    }


}
