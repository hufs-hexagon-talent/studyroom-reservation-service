package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
public class ReservationRequestDto { // CR
    private Long roomId;
    private Instant startDateTime;
    private Instant endDateTime;

    public ReservationRequestDto(Long roomId, Instant startDateTime, Instant endDateTime) {
        this.roomId = roomId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    public Reservation toEntity(User user, Room room) {
        return Reservation.builder()
                .user(user)
                .room(room)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(ReservationState.NOT_VISITED)
                .build();
    }


}
