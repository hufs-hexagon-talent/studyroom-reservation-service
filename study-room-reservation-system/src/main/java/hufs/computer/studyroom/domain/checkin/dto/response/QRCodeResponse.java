package hufs.computer.studyroom.domain.checkin.dto.response;

import java.time.Instant;

public record QRCodeResponse(
        String verificationCode,
        Instant expiresAt
) {
}
