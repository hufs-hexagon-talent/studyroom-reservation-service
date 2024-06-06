package com.test.studyroomreservationsystem.dto.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class SpecificRoomsReservationsDto {

    private final List<RoomReservation> reservations;

    public SpecificRoomsReservationsDto(List<RoomReservation> reservations) {
        this.reservations = reservations;
    }

    @Getter
    @Builder
    public static class RoomReservation {
        private final Long reservationId;
        private final Long roomId;
        private final String roomName;
        private final Long userId;
        private final Instant startDateTime;
        private final Instant endDateTime;

        public RoomReservation(Long reservationId, Long roomId, String roomName, Long userId, Instant startDateTime, Instant endDateTime) {
            this.reservationId = reservationId;
            this.roomId = roomId;
            this.roomName = roomName;
            this.userId = userId;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }
    }

}
