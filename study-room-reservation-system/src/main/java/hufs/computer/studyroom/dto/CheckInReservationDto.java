package hufs.computer.studyroom.dto;

import hufs.computer.studyroom.domain.entity.Reservation.ReservationState;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CheckInReservationDto {
    private Long reservationId;
    private Long userId;
    private String name;
    private Long roomPartitionId;
    private String roomName;
    private String partitionNumber;
    private Instant reservationStartTime;
    private Instant reservationEndTime;
    private ReservationState state;

    public CheckInReservationDto(Long reservationId,
                                 Long userId,
                                 String name,
                                 Long roomPartitionId,
                                 String roomName,
                                 String partitionNumber,
                                 Instant reservationStartTime,
                                 Instant reservationEndTime,
                                 ReservationState state) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.name = name;
        this.roomPartitionId = roomPartitionId;
        this.roomName = roomName;
        this.partitionNumber = partitionNumber;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.state = state;
    }
}
