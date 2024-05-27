package com.test.studyroomreservationsystem.dto.room;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RoomsResponseDto {
    private final Long roomId;
    private final String roomName;
    private final RoomOperationPolicy policy;
//    private final TimeRange timeline;


    public RoomsResponseDto(Long roomId, String roomName, RoomOperationPolicy policy) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.policy = policy;
//        this.timeline = timeline;
    }
//    @Getter
//    public static class TimeRange {
//        private final LocalDateTime startDateTime;
//        private final LocalDateTime endDateTime;
//
//
//        public TimeRange( LocalDateTime startDateTime, LocalDateTime endDateTime) {
//            this.startDateTime = startDateTime;
//            this.endDateTime = endDateTime;
//       }
}



