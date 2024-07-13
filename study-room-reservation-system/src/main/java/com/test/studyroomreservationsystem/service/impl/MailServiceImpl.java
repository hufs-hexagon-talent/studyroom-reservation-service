package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dto.auth.EmailResponseDto;
import com.test.studyroomreservationsystem.exception.EmailCreationException;
import com.test.studyroomreservationsystem.service.MailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final Random random = new Random();
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private int number;

    @Override
    public void generateRandomNumber() {
        number = random.nextInt(900000) + 100000;
    }


    @Override
    public MimeMessage createMail(String recipientEmail) {
        generateRandomNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(Message.RecipientType.TO, recipientEmail);
            message.setSubject("[이메일 인증]");

            String body = "";

            body += "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>";
            body += "<h2 style='color: #2E86C1; text-align: center;'>이메일 인증</h2>";
            body += "<p style='font-size: 16px; color: #333;'>안녕하세요,</p>";
            body += "<p style='font-size: 16px; color: #333;'>요청하신 인증 번호를 보내드립니다.</p>";
            body += "<div style='text-align: center; margin: 20px 0;'>";
            body += "<h1 style='font-size: 48px; color: #E74C3C;'>" + number + "</h1>";
            body += "</div>";
            body += "<p style='font-size: 16px; color: #333;'>감사합니다.</p>";
            body += "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>";
            body += "<p style='font-size: 12px; color: #999; text-align: center;'>이 메일은 자동으로 생성된 메일입니다. 회신하지 마세요.</p>";
            body += "</div>";

            message.setText(body, "UTF-8", "html");

        } catch (MessagingException e) {
            log.error("Failed to create email message", e);
            throw new EmailCreationException(e);
        }
        return message;
    }

    @Override
    public int sendMail(String recipentEmail) {
        MimeMessage mail = createMail(recipentEmail);
        javaMailSender.send(mail);
        return number;
    }


}
