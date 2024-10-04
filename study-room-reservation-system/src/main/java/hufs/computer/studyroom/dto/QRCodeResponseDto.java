package hufs.computer.studyroom.dto;

import lombok.Getter;

import java.time.Instant;

@Getter
public class QRCodeResponseDto {
    private final String verificationCode;
    private final Instant expiresAt;

    public QRCodeResponseDto(String verificationCode, Instant expiresAt) {
        this.verificationCode = verificationCode;
        this.expiresAt = expiresAt;
    }
}
