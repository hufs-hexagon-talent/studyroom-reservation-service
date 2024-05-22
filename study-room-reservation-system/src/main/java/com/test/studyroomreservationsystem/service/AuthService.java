package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.dto.auth.LoginRequestDto;
import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {
    LoginResponseDto authenticate(LoginRequestDto loginRequestDto) throws AuthenticationException;
}
