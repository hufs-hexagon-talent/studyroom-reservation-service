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

            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");

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
