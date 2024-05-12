//package com.test.studyroomreservationsystem.security.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.CredentialsExpiredException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
//        Map<String, Object> body = new HashMap<>();
//        body.put("success", false);
//        HttpStatus status = HttpStatus.UNAUTHORIZED;
//
//        if (exception instanceof BadCredentialsException) {
//
//            body.put("error", "Invalid credentials");
//            log.info("Login failed: Incorrect username or password");
//        } else if (exception instanceof DisabledException) {
//
//            body.put("error", "Account is locked");
//            status = HttpStatus.LOCKED;
//            log.info("Login failed: Account is locked");
//        } else if (exception instanceof CredentialsExpiredException) {
//
//            body.put("error", "Credentials expired");
//            status = HttpStatus.FORBIDDEN;
//            log.info("Login failed: Credentials have expired");
//        } else {
//
//            body.put("error", "Authentication failed");
//            log.info("Login failed: Unknown authentication error");
//        }
//
//        response.setStatus(status.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        objectMapper.writeValue(response.getWriter(), body);
//    }
//}