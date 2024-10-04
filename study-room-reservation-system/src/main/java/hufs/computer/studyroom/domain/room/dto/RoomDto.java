package hufs.computer.studyroom.domain.room.dto;


import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.entity.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Getter
public class RoomDto {
    private Long roomId;
    private String roomName;

    public RoomDto(Long roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }


    public Room toEntity() {
        return Room.builder()
                .roomId(roomId)
                .roomName(roomName)
                .build();
    }
}
