package com.test.studyroomreservationsystem.service;

import jakarta.mail.internet.MimeMessage;

public interface MailService {
    String generateAuthCode(); // 인증코드 생성
    MimeMessage createMailContext(String recipientEmail, String authCode); // 메일 내용 생성
    MimeMessage createMail(String recipientEmail); // 메일 생성
    void sendMail(MimeMessage mail); // 메일 전송
    boolean validateAuthCode(String recipientEmail, String authCode);
}
