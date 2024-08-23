package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Reservation.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.service.DateTimeUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
public class ReservationRequestDto { // CR
    private Long roomPartitionId;
    private Instant startDateTime;
    private Instant endDateTime;

    public ReservationRequestDto(Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
        this.roomPartitionId = roomPartitionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Reservation toEntity(User user, RoomPartition roomPartition) {
        return Reservation.builder()
                .user(user)
                .roomPartition(roomPartition)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(ReservationState.NOT_VISITED)
                .build();
    }


}
