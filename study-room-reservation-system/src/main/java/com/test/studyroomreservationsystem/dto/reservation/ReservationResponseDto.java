package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter

public class ReservationResponseDto {
    private Long reservationId;
    private Long roomId;
    private String roomName;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    @Builder
    public ReservationResponseDto(Long reservationId,Long roomId, String roomName, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
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
