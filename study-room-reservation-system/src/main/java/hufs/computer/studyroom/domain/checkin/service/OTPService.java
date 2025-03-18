package hufs.computer.studyroom.domain.checkin.service;

import hufs.computer.studyroom.common.util.JsonConverterUtil;
import hufs.computer.studyroom.domain.auth.dto.OTPInfo;
import hufs.computer.studyroom.domain.checkin.dto.response.QRCodeResponse;
import hufs.computer.studyroom.common.redis.RedisService;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static hufs.computer.studyroom.common.redis.RedisKeyConstants.OTP_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPService {
    @Value("${spring.service.otpTTL}") private Integer otpTTL;

    private final RedisService redisService;

    private final JsonConverterUtil jsonConverterUtil;

    public QRCodeResponse generateVerificationCode(CustomUserDetails currentUser) {
        String verificationId = OTP_PREFIX + UUID.randomUUID();

        Long userId = currentUser.getUser().getUserId();
        OTPInfo otpInfo = new OTPInfo(userId);

        String otpInfoJson = jsonConverterUtil.serializeAuthInfo(otpInfo);
        Duration expiration = Duration.ofSeconds(otpTTL);

        redisService.setValues(verificationId, otpInfoJson, expiration);

        Instant expiresAt = Instant.now().plus(expiration);
        return new QRCodeResponse(verificationId, expiresAt);
    }


}
