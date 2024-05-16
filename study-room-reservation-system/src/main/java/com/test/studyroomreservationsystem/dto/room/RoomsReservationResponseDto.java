package com.test.studyroomreservationsystem.dto.room;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RoomsReservationResponseDto {
    private final Long roomId;
    private final List<TimeRange> timeline;


    public RoomsReservationResponseDto(Long roomId, List<TimeRange> timeline) {
        this.roomId = roomId;
        this.timeline = timeline;
    }
    @Getter
    public static class TimeRange {
        private final LocalDateTime startDateTime;
        private final LocalDateTime endDateTime;

        public TimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }
    }

}
