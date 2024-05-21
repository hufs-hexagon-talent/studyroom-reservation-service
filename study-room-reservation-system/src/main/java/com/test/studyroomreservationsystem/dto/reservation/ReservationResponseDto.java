package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class ReservationResponseDto {
    private Long roomId;
    private String roomName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder
    public ReservationResponseDto(Long roomId, String roomName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    public Reservation toEntity(User user, Room room) {
        return Reservation.builder()
                .user(user)
                .room(room)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(ReservationState.RESERVED)
                .build();
    }


}
