package hufs.computer.studyroom.dto.reservation;

import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class PartitionsReservationResponseDto {
    private final Long partitionId;
    private final Long roomId;
    private final String roomName;
    private final String partitionNumber;
    private final RoomOperationPolicy policy;
    private final List<TimeRange> timeline;

    public PartitionsReservationResponseDto(Long partitionId,
                                            Long roomId,
                                            String roomName,
                                            String partitionNumber,
                                            RoomOperationPolicy policy,
                                            List<TimeRange> timeline) {
        this.partitionId = partitionId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.partitionNumber = partitionNumber;
        this.policy = policy;
        this.timeline = timeline;
    }

    @Getter
    public static class TimeRange {
        private final Long reservationId;
        private final Instant startDateTime;
        private final Instant endDateTime;


        public TimeRange(Long reservationId, Instant startDateTime, Instant endDateTime) {
            this.reservationId = reservationId;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }
    }
}
