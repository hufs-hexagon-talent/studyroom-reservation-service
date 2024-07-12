package com.test.studyroomreservationsystem.apicontroller.auth;

import com.test.studyroomreservationsystem.service.MailService;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/mail/send")
    public ResponseEntity<String> sendMail(@RequestParam String username) {
        String email = userService.findEmailByUsername(username);
//        int verificationCode = mailService.sendMail(email);
        mailService.sendMail(email);
        return ResponseEntity.ok("Verification code sent to " + email);
    }

}
