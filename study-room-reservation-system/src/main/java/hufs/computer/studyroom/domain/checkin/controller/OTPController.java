package hufs.computer.studyroom.domain.checkin.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.checkin.service.OTPService;
import hufs.computer.studyroom.domain.checkin.dto.response.QRCodeResponse;
import hufs.computer.studyroom.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Check-In", description = "예약한 정보를 방문 처리 하는 API")
@RestController
@RequestMapping("/check-in")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;

    @Operation(summary = "✅ QR code 생성, OTP 제공",
            description = "로그인된 유저에 대해서 OTP 제공",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/otp")
    public ResponseEntity<SuccessResponse<QRCodeResponse>> generateVerificationCode(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        var result = otpService.generateVerificationCode(currentUser);
        return ResponseFactory.created(result);
    }
}
