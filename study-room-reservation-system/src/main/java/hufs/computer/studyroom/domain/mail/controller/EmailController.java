package hufs.computer.studyroom.domain.mail.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import hufs.computer.studyroom.domain.mail.dto.response.EmailResponse;
import hufs.computer.studyroom.domain.mail.dto.request.EmailVerifyRequest;
import hufs.computer.studyroom.domain.mail.dto.response.EmailVerifyResponse;
import hufs.computer.studyroom.domain.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth", description = "인증 관련")
public class EmailController {

    private final MailService mailService;

    @Operation(summary = "❌ 이메일 인증 코드 전송",
            description = "로그인X, 비밀번호 수정을 위한 이메일 인증 코드 전송 API")
    @PostMapping("/mail/send")
    public ResponseEntity<SuccessResponse<EmailResponse>> sendAuthCode(
             @ExistUser(checkType = ExistUser.CheckType.USERNAME) @RequestParam String username) {

        var result = mailService.sendAuthCode(username);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "❌ 인증 코드 검증",
            description = "이메일로 전송된 인증 코드를 검증하는 API")
    @PostMapping("/mail/verify")
    public ResponseEntity<SuccessResponse<EmailVerifyResponse>> verifyMail(@Valid @RequestBody EmailVerifyRequest request) {

        var result = mailService.verifyMail(request);
        return ResponseFactory.success(result);
    }
}