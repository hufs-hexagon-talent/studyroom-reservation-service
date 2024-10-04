package hufs.computer.studyroom.domain.reservation.dto;

import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ReservationInfoResponseDto {
    private Long reservationId;
    private Long userId;
    private Long roomId;
    private String roomName;
    private Long roomPartitionId;
    private String partitionNumber;
    private Instant startDateTime;
    private Instant endDateTime;
    private ReservationState reservationState;
    private Instant createAt;
    private Instant updateAt;

    public Reservation toEntity(User user, RoomPartition roomPartition) {
        return Reservation.builder()
                .reservationId(reservationId)
                .user(user)
                .roomPartition(roomPartition)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(reservationState)
                .build();
    }


}
