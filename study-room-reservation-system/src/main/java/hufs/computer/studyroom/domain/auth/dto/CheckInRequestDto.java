package hufs.computer.studyroom.domain.auth.dto;

import lombok.Getter;

@Getter
public class CheckInRequestDto {

    private String verificationCode; // 유저 정보
    private Long roomId; // 여러 파티션들에 대한 ID

    public CheckInRequestDto(String verificationCode, Long roomId) {
        this.verificationCode = verificationCode;
        this.roomId = roomId;
    }
}