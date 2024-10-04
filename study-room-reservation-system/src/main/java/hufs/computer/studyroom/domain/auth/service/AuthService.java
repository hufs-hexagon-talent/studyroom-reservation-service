package hufs.computer.studyroom.domain.auth.service;

import hufs.computer.studyroom.domain.auth.dto.LoginRequestDto;
import hufs.computer.studyroom.domain.auth.dto.LoginResponseDto;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {
    LoginResponseDto authenticate(LoginRequestDto loginRequestDto) throws AuthenticationException;
}
