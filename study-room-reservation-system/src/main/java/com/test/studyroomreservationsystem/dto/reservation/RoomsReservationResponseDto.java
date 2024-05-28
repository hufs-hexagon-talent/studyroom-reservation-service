package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class RoomsReservationResponseDto {
    private final Long roomId;
    private final String roomName;
    private final RoomOperationPolicy policy;
    private final List<TimeRange> timeline;


    public RoomsReservationResponseDto(Long roomId, String roomName, RoomOperationPolicy policy, List<TimeRange> timeline) {
        this.roomId = roomId;
        this.roomName = roomName;
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
