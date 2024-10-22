package hufs.computer.studyroom.domain.checkin;

import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
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

//todo : 체크인 응답 시 어떤 필드가 필요한지 논의
//    public CheckInReservationDto(Long reservationId,
//                                 Long userId,
//                                 String name,
//                                 Long roomPartitionId,
//                                 String roomName,
//                                 String partitionNumber,
//                                 Instant reservationStartTime,
//                                 Instant reservationEndTime,
//                                 ReservationState state) {
//        this.reservationId = reservationId;
//        this.userId = userId;
//        this.name = name;
//        this.roomPartitionId = roomPartitionId;
//        this.roomName = roomName;
//        this.partitionNumber = partitionNumber;
//        this.reservationStartTime = reservationStartTime;
//        this.reservationEndTime = reservationEndTime;
//        this.state = state;
//    }
}
