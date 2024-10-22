package hufs.computer.studyroom.domain.checkin.service;

import hufs.computer.studyroom.domain.checkin.dto.response.QRCodeResponse;
import hufs.computer.studyroom.domain.auth.service.RedisService;
import hufs.computer.studyroom.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPService {
    @Value("${spring.service.randomChars}") private String randomChars;
    @Value("${spring.service.otpTTL}") private Integer otpTTL;
    @Value("${spring.service.otpLength}") private Integer otpLength;

    private final RedisService redisService;
    private final Random random = new Random();

    public QRCodeResponse generateVerificationCode(CustomUserDetails currentUser) {
        String verificationCode = generateRandomString(otpLength);
        Duration expiration = Duration.ofSeconds(otpTTL);
        redisService.setValues(verificationCode, currentUser.getUser().getUserId().toString(), expiration);

        Instant expiresAt = Instant.now().plus(expiration);
        return new QRCodeResponse(verificationCode, expiresAt);
    }

    private String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(randomChars.charAt(random.nextInt(randomChars.length())));
        }
        return builder.toString();
    }

}
