package hufs.computer.studyroom.domain.auth.controller;

import hufs.computer.studyroom.common.util.ApiResponseDto;
import hufs.computer.studyroom.domain.auth.dto.QRCodeResponseDto;
import hufs.computer.studyroom.security.CustomUserDetails;
import hufs.computer.studyroom.domain.auth.service.OTPCodeService;
import hufs.computer.studyroom.common.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
@Tag(name = "OTP", description = "OTP 제공 관련 API")
@RestController
@RequestMapping("/otp")
public class OTPController {
    private final RedisService redisService;
    private final OTPCodeService qrCodeService;

    @Value("${spring.service.otpTTL}")
    private Integer otpTTL;
    @Value("${spring.service.otpLength}")
    private Integer otpLength;

    public OTPController(RedisService redisService, OTPCodeService qrCodeService) {
        this.redisService = redisService;
        this.qrCodeService = qrCodeService;
    }

    @Operation(summary = "✅ QR code 생성, OTP 제공",
            description = "로그인된 유저에 대해서 OTP 제공",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<QRCodeResponseDto>> generateVerificationCode(@AuthenticationPrincipal CustomUserDetails currentUser) {
        String verificationCode = qrCodeService.generateRandomString(otpLength);
        Duration expiration = Duration.ofSeconds(otpTTL);
        redisService.setValues(verificationCode, currentUser.getUser().getUserId().toString(), expiration);

        Instant expiresAt = Instant.now().plus(expiration);
        QRCodeResponseDto qrCode = new QRCodeResponseDto(verificationCode, expiresAt);

        ApiResponseDto<QRCodeResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", qrCode);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
