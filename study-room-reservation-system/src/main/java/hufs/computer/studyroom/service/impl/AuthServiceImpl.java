package hufs.computer.studyroom.service.impl;

import hufs.computer.studyroom.dto.auth.LoginRequestDto;
import hufs.computer.studyroom.dto.auth.LoginResponseDto;
import hufs.computer.studyroom.service.AuthService;
import hufs.computer.studyroom.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) throws AuthenticationException {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authToken);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = tokenService.createAccessToken(username, role);
        String refreshToken = tokenService.createRefreshToken(username);

        return new LoginResponseDto(accessToken, refreshToken);
    }
}
