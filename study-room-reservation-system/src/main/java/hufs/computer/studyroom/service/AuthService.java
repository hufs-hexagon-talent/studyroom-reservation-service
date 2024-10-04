package hufs.computer.studyroom.service;

import hufs.computer.studyroom.dto.auth.LoginRequestDto;
import hufs.computer.studyroom.dto.auth.LoginResponseDto;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {
    LoginResponseDto authenticate(LoginRequestDto loginRequestDto) throws AuthenticationException;
}
