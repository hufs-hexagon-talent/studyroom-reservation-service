package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.dto.auth.EmailResponseDto;
import jakarta.mail.internet.MimeMessage;

public interface MailService {
    void generateRandomNumber();
    MimeMessage createMail(String recipientEmail);
    int sendMail(String recipientEmail);

}
