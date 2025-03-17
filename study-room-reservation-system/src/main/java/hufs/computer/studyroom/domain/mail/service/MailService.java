package hufs.computer.studyroom.domain.mail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.service.RedisService;
import hufs.computer.studyroom.domain.auth.service.JWTService;
import hufs.computer.studyroom.domain.mail.dto.AuthInfo;
import hufs.computer.studyroom.domain.mail.dto.request.EmailVerifyRequest;
import hufs.computer.studyroom.domain.mail.dto.response.EmailResponse;
import hufs.computer.studyroom.domain.mail.dto.response.EmailVerifyResponse;
import hufs.computer.studyroom.domain.mail.mapper.MailMapper;
import hufs.computer.studyroom.domain.user.dto.request.VerifyEmailRequest;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final Random random = new Random();
    private final UserQueryService userQueryService;
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    private final MailMapper mailMapper;
    private final SpringTemplateEngine templateEngine;
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;

    // 인증번호 만료 시간 5분
    @Value("${spring.service.authCodeExpiryTime}") private int authCodeExpiryTime;
    @Value("${spring.mail.username}") private String senderEmail;
    private static final String MAIL_PREFIX = "mail:";

    public EmailResponse sendAuthCode(String username) {
        String email = userQueryService.findByUsername(username).getEmail();
        String authCode = generateAuthCode();
        String verificationId = MAIL_PREFIX + UUID.randomUUID();

        String authInfoJson = serializeAuthInfo(new AuthInfo(email,authCode));

        redisService.setValues(verificationId, authInfoJson, Duration.ofMinutes(authCodeExpiryTime));

//      메일 생성
        MimeMessage message = createMailContext(email, authCode);
//      메일 전송
        javaMailSender.send(message);

        return mailMapper.toEmailResponse(email);
    }

    public EmailResponse sendAuthCodeToEmail(String email){
        String authCode = generateAuthCode();
        String authInfoJson = serializeAuthInfo(new AuthInfo(email,authCode));

        String verificationId = MAIL_PREFIX + UUID.randomUUID();
        redisService.setValues(verificationId, authInfoJson, Duration.ofMinutes(authCodeExpiryTime));

//      메일 생성
        MimeMessage message = createMailContext(email, authCode);
//      메일 전송
        javaMailSender.send(message);

        return mailMapper.toEmailResponse(email);
    }

    public EmailVerifyResponse verifyMailForPassword(EmailVerifyRequest request) {
        String verificationId = request.verificationId();
        String storedAuthInfo = redisService.getValue(verificationId);

//      인증 정보 검증
        if (storedAuthInfo == null || storedAuthInfo.isEmpty()) {
            throw new CustomException(AuthErrorCode.INVALID_AUTH_INFO);
        }

        AuthInfo authInfo = deserializeAuthInfo(storedAuthInfo);

        String storedEmail = authInfo.email();
        String storedAuthCode = authInfo.authCode();

//      인증 코드 검증

        if (!storedAuthCode.equals(request.verifyCode())) {
            throw new CustomException(AuthErrorCode.AUTH_CODE_MISMATCH);
        }
        redisService.deleteValue(verificationId);
        String passwordResetToken = jwtService.createPasswordResetToken(storedEmail);

        return mailMapper.toEmailVerifyResponse(storedEmail, passwordResetToken);
    }

    public String verifyMailForMail(VerifyEmailRequest request){
        String verificationId = request.verificationId();

        String storedAuthInfo = redisService.getValue(verificationId);

        if (storedAuthInfo == null || storedAuthInfo.isEmpty()) {
            throw new CustomException(AuthErrorCode.INVALID_AUTH_INFO);
        }

        AuthInfo authInfo = deserializeAuthInfo(storedAuthInfo);

        String storedEmail = authInfo.email();
        String storedAuthCode = authInfo.authCode();

        //      인증 코드 검증
        if (!storedAuthCode.equals(request.verifyCode())) {
            throw new CustomException(AuthErrorCode.AUTH_CODE_MISMATCH);
        }
        redisService.deleteValue(verificationId);

        return storedEmail;
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

    /**
    * 직렬화
    */
    private String serializeAuthInfo(AuthInfo authInfo) {
        try {
            return objectMapper.writeValueAsString(authInfo);
        } catch (JsonProcessingException e) {
            throw new CustomException(AuthErrorCode.SERIALIZATION_FAILED);
        }
    }

    /**
    * 역직렬화
    */
    private AuthInfo deserializeAuthInfo(String storedAuthInfo) {
        try {
            return objectMapper.readValue(storedAuthInfo, AuthInfo.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(AuthErrorCode.DESERIALIZATION_FAILED);
        }
    }
}