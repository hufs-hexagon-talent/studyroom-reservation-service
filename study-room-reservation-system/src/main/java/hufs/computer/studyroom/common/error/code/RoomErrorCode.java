package hufs.computer.studyroom.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum RoomErrorCode implements ErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM-001", "해당 방은 존재하지 않습니다."),
    ROOM_ALREADY_EXISTS(HttpStatus.CONFLICT, "ROOM-002", "이미 존재하는 방입니다."),
    PARTITION_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM-003", "해당 파티션은 존재하지 않습니다."),
    PARTITION_ALREADY_EXISTS(HttpStatus.CONFLICT, "ROOM-004", "이미 존재하는 파티션입니다."),
    ROOM_HAS_NOT_PARTITION(HttpStatus.NOT_FOUND, "ROOM-005", "해당 룸에는 파티션이 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
