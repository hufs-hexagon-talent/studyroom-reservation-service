package com.test.studyroomreservationsystem.dto.room;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class RoomsResponseDto {
    private final Long roomId;
    private final String roomName;
    private final Long policyId;
    private final List<TimeRange> timeline;


    public RoomsResponseDto(Long roomId, String roomName, Long policyId, List<TimeRange> timeline) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.policyId = policyId;
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
