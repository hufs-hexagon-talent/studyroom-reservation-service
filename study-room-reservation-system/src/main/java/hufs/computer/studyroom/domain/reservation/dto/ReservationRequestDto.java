package hufs.computer.studyroom.domain.reservation.dto;

import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.user.entity.User;
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
