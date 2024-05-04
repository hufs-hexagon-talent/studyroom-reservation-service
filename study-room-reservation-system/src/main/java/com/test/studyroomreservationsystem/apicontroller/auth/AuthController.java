package com.test.studyroomreservationsystem.apicontroller.auth;

import com.test.studyroomreservationsystem.dto.auth.SignInDto;
import com.test.studyroomreservationsystem.dto.jwt.JwtToken;
import com.test.studyroomreservationsystem.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String loginId = signInDto.getLoginId();
        String password = signInDto.getPassword();
        JwtToken jwtToken = authService.signIn(loginId, password);
        log.info("request username = {}, password = {}", loginId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
