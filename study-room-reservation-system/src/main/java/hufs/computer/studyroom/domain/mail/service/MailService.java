package hufs.computer.studyroom.domain.mail.service;

import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.error.exception.todo.AuthCodeMismatchException;
import hufs.computer.studyroom.common.error.exception.todo.EmailCreationException;
import hufs.computer.studyroom.common.service.RedisService;
import hufs.computer.studyroom.domain.mail.dto.request.EmailVerifyRequest;
import hufs.computer.studyroom.domain.mail.dto.response.EmailResponse;
import hufs.computer.studyroom.domain.mail.dto.response.EmailVerifyResponse;
import hufs.computer.studyroom.domain.mail.mapper.MailMapper;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import hufs.computer.studyroom.security.TokenService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final Random random = new Random();
    private final UserQueryService userQueryService;
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    private final MailMapper mailMapper;
    private final TokenService tokenService;
    private final SpringTemplateEngine templateEngine;

    // 인증번호 만료 시간 5분
    @Value("${spring.service.authCodeExpiryTime}") private Duration authCodeExpiryTime;
    @Value("${spring.mail.username}") private String senderEmail;


    public EmailResponse sendAuthCode(String username) {
        String email = userQueryService.findByUsername(username).getEmail();
        String authCode = generateAuthCode();
        redisService.setValues(email, authCode, authCodeExpiryTime);

//      메일 생성
        MimeMessage message = createMailContext(email, authCode);
//      메일 전송
        javaMailSender.send(message);

        return mailMapper.toEmailResponse(email);
    }

    public EmailVerifyResponse verifyMail(EmailVerifyRequest request) {
        String email = request.email();
        String storedAuthCode = redisService.getValue(email);

//      인증 코드 검증
        if (!storedAuthCode.equals(request.verifyCode())) {
            throw new CustomException(AuthErrorCode.AUTH_CODE_MISMATCH);
        }
        redisService.deleteValue(email);
        String passwordResetToken = tokenService.createPasswordResetToken(email);

        return mailMapper.toEmailVerifyResponse(email, passwordResetToken);
    }

    /**
     * 메일 내용 생성
     */
    private MimeMessage createMailContext(String recipientEmail, String authCode) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(Message.RecipientType.TO, recipientEmail);
            message.setSubject("[이메일 인증]");

//          이메일 본문 생성 (Thymeleaf 사용)
            Context context = new Context();
            context.setVariable("authCode", authCode);
            String body = templateEngine.process("email-verification", context);

            message.setText(body, "UTF-8", "html");

        } catch (MessagingException e) {
            throw new CustomException(AuthErrorCode.EMAIL_CREATION_FAILED);
        }
        return message;
    }

    /**
     * 인증코드 생성
     */
    private String generateAuthCode() {
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}