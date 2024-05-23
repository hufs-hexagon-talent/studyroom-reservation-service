package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dto.auth.LoginRequestDto;
import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
import com.test.studyroomreservationsystem.service.AuthService;
import com.test.studyroomreservationsystem.service.TokenService;
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
