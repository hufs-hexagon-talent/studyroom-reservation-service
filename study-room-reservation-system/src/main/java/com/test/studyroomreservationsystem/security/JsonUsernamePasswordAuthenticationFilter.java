package com.test.studyroomreservationsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/sing-in";  // /login/oauth2/ + ????? 로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST";    //HTTP 메서드 방식은 POST
    private static final String CONTENT_TYPE = "application/json";//json 타입의 데이터로만 로그인을 진행한다.
    private final ObjectMapper objectMapper;
    private static final String USERNAME_KEY="loginId";
    private static final String PASSWORD_KEY="password";

    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); //=>   /sing-in 의 요청에, POST로 온 요청에 매칭

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {

        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  /oauth2/sing-in/* 의 요청에, GET으로 온 요청을 처리하기 위해 설정

        this.objectMapper = objectMapper;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String username = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);//principal 과 credentials 전달

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
