package hufs.computer.studyroom.domain.room.dto;

import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RoomResponseDto {
    private final Long roomId;
    private final String roomName;
    private final RoomOperationPolicy policy;
//    private final TimeRange timeline;


    public RoomResponseDto(Long roomId, String roomName, RoomOperationPolicy policy) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.policy = policy;
    }
}



