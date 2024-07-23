package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
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
        private final Long roomPartitionId;
        private final String partitionNumber;
        private final Long userId;
        private final String name;
        private final ReservationState state;
        private final Instant startDateTime;
        private final Instant endDateTime;

        public RoomReservation(Long reservationId, Long roomId, String roomName, Long roomPartitionId, String partitionNumber, Long userId, String name, ReservationState state, Instant startDateTime, Instant endDateTime) {
            this.reservationId = reservationId;
            this.roomId = roomId;
            this.roomName = roomName;
            this.roomPartitionId = roomPartitionId;
            this.partitionNumber = partitionNumber;
            this.userId = userId;
            this.name = name;
            this.state = state;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }
    }

}
