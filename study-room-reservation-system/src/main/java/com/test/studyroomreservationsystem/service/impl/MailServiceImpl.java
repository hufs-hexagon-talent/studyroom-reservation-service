package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.exception.AuthCodeMismatchException;
import com.test.studyroomreservationsystem.exception.EmailCreationException;
import com.test.studyroomreservationsystem.service.MailService;
import com.test.studyroomreservationsystem.service.RedisService;
import com.test.studyroomreservationsystem.service.TokenService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final Random random = new Random();
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 인증번호 만료 시간 5분
    private final Duration AUTH_CODE_EXPIRY_TIME = Duration.ofMinutes(5); // todo : 나중에 @Value로 빼기

    @Override
    public String generateAuthCode() {
        return String.valueOf(random.nextInt(900000) + 100000);
    }
    @Override
    public MimeMessage createMailContext(String recipientEmail, String authCode) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(Message.RecipientType.TO, recipientEmail);
            message.setSubject("[이메일 인증]");

            //todo : 하드 코딩 된거 변경하기
            String body = new StringBuilder()
                    .append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>")
                    .append("<h2 style='color: #2E86C1; text-align: center;'>이메일 인증</h2>")
                    .append("<p style='font-size: 16px; color: #333;'>안녕하세요,</p>")
                    .append("<p style='font-size: 16px; color: #333;'>요청하신 인증 번호를 보내드립니다.</p>")
                    .append("<div style='text-align: center; margin: 20px 0;'>")
                    .append("<h1 style='font-size: 48px; color: #E74C3C;'>").append(authCode).append("</h1>")
                    .append("</div>")
                    .append("<p style='font-size: 16px; color: #333;'>감사합니다.</p>")
                    .append("<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>")
                    .append("<p style='font-size: 12px; color: #999; text-align: center;'>이 메일은 자동으로 생성된 메일입니다. 회신하지 마세요.</p>")
                    .append("</div>")
                    .toString();

            message.setText(body, "UTF-8", "html");

        } catch (MessagingException e) {
            log.error("Failed to create email message", e);
            throw new EmailCreationException(e);
        }
        return message;
    }

    @Override
    public MimeMessage createMail(String recipientEmail) {
        String authCode = generateAuthCode();
        redisService.setValues(recipientEmail,authCode,AUTH_CODE_EXPIRY_TIME);
        return createMailContext(recipientEmail,authCode);
    }
    @Override
    public void sendMail(MimeMessage mail) {javaMailSender.send(mail);}

    @Override
    public boolean validateAuthCode(String recipientEmail, String authCode) { // todo: 프로시져로 구성할지 ? 고민됨
        String storedAuthCode = redisService.getValue(recipientEmail);
        if (storedAuthCode == null) {
            throw new RuntimeException(); // todo MailAuthCodeNotFoundException 예외로 관리하기
        }
        if (!storedAuthCode.equals(authCode)) {
            throw new AuthCodeMismatchException();
        }

        redisService.deleteValue(recipientEmail); // 사용 후 삭제
        return true;
    }
    //
}
