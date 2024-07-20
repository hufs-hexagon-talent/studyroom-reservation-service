package com.test.studyroomreservationsystem.apicontroller.auth;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.EmailVerifyRequestDto;
import com.test.studyroomreservationsystem.dto.EmailVerifyResponseDto;
import com.test.studyroomreservationsystem.dto.user.UserInfoResponseDto;
import com.test.studyroomreservationsystem.dto.user.UserPasswordInfoResetRequestDto;
import com.test.studyroomreservationsystem.dto.auth.EmailResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
import com.test.studyroomreservationsystem.service.MailService;
import com.test.studyroomreservationsystem.service.TokenService;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련")
public class EmailController {

    private final MailService mailService;
    private final UserService userService;
    private final TokenService tokenService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "✅ 이메일 인증 코드 전송",
            description = "로그인X, 비밀번호 수정을 위한 이메일 인증 코드 전송 API"
    )
    @PostMapping("/mail/send")
    public ResponseEntity<ApiResponseDto<EmailResponseDto>> sendAuthCode(@RequestParam String username) {
        String email = userService.findByUsername(username).getEmail();
        MimeMessage newMail = mailService.createMail(email);
        mailService.sendMail(newMail);

        EmailResponseDto emailDto = new EmailResponseDto(email);

        ApiResponseDto<EmailResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "인증번호가 이메일로 전송되었습니다.", emailDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "🚧 인증 코드 검증",
            description = "이메일로 전송된 인증 코드를 검증하는 API")
    @PostMapping("/mail/verify")
    public ResponseEntity<ApiResponseDto<EmailVerifyResponseDto>> verifyMail(@RequestBody EmailVerifyRequestDto requestDto) {
        String email = requestDto.getEmail();
        String verifyCode = requestDto.getVerifyCode();

        mailService.validateAuthCode(email, verifyCode); //서비스 계층에서 예외처리
        String passwordResetToken = tokenService.createPasswordResetToken(email); // 토큰 발급

        // todo : 인증완료되었으면 비밀번호 변경가능함을 증명해줄 토큰 발급(인가)

        EmailVerifyResponseDto emailDto = new EmailVerifyResponseDto(email, passwordResetToken);
        ApiResponseDto<EmailVerifyResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "Email 인증에 성공하였습니다.", emailDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "✅ 비밀번호 재설정",
            description = "JWT 토큰과 새로운 비밀번호를 사용하여 비밀번호를 재설정하는 API")
    @PostMapping("/mail/reset-password")
    public ResponseEntity<ApiResponseDto<UserInfoResponseDto>> resetPassword(@RequestBody UserPasswordInfoResetRequestDto requestDto) {
        String token = requestDto.getToken();

        String email = jwtUtil.getEmail(token); // 토큰에서 이메일 추출
        Long userId = userService.findByEmail(email).getUserId();
        User user = userService.updateUserPassword(userId, requestDto);// 비밀번호 업데이트
        UserInfoResponseDto responseDto = userService.dtoFrom(user);
        ApiResponseDto<UserInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "비밀번호가 성공적으로 변경되었습니다.",responseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
