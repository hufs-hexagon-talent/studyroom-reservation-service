package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.dto.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.QRCodeResponseDto;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.QRCodeService;
import com.test.studyroomreservationsystem.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class OTPCodeController {
    private final RedisService redisService;
    private final QRCodeService qrCodeService;

    public OTPCodeController(RedisService redisService, QRCodeService qrCodeService) {
        this.redisService = redisService;
        this.qrCodeService = qrCodeService;
    }

    @Operation(summary = "✅ QR code 생성, OTP 제공",
            description = "로그인된 유저에 대해서 OTP 제공",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<QRCodeResponseDto>> generateVerificationCode(@AuthenticationPrincipal CustomUserDetails currentUser) {
        String verificationCode = qrCodeService.generateRandomString(8);
        Duration expiration = Duration.ofSeconds(30);
        redisService.setValues(verificationCode, currentUser.getUser().getUserId().toString(), expiration);

        Instant expiresAt = Instant.now().plus(expiration);
        QRCodeResponseDto qrCode = new QRCodeResponseDto(verificationCode, expiresAt);

        ApiResponseDto<QRCodeResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", qrCode);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
