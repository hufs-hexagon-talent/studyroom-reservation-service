package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter

public class ReservationResponseDto {
    private Long reservationId;
    private Long roomId;
    private String roomName;
    private Instant startDateTime;
    private Instant endDateTime;

    @Builder
    public ReservationResponseDto(Long reservationId,Long roomId, String roomName, Instant startDateTime, Instant endDateTime) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    public Reservation toEntity(User user, Room room) {
        return Reservation.builder()
                .reservationId(reservationId)
                .user(user)
                .room(room)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(ReservationState.RESERVED)
                .build();
    }


}
