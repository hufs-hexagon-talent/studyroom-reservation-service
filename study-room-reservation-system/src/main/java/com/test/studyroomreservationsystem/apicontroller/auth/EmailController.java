package com.test.studyroomreservationsystem.apicontroller.auth;

import com.test.studyroomreservationsystem.dto.auth.EmailResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.service.MailService;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련")
public class EmailController {

    private final MailService mailService;
    private final UserService userService;

    @Operation(summary = "✅ 이메일 인증 코드 전송",
            description = "로그인X, 비밀번호 수정을 위한 이메일 인증 코드 전송 API"
    )
    @PostMapping("/mail/send")
    public ResponseEntity<ApiResponseDto<EmailResponseDto>> sendMail(@RequestParam String username) {
        String email = userService.findEmailByUsername(username);
        int verificationCode = mailService.sendMail(email);
        EmailResponseDto emailDto = new EmailResponseDto(email, verificationCode);
        mailService.sendMail(email);

        ApiResponseDto<EmailResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "인증 코드 전송 성공하였습니다.", emailDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
