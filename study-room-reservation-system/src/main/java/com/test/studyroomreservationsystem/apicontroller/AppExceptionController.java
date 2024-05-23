package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionController {

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponseDto errorResponse
                = new ErrorResponseDto(HttpStatus.UNAUTHORIZED.toString(), "[Unauthorized] API에 접근하려면 인증이 필요합니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationServiceException(AuthenticationServiceException ex) {
        ErrorResponseDto errorResponse
                = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "[Internal Server Error] 서비스에 문제가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

/*
 todo : filter 에서 권한이 부족한 경우 JwtAccessDeniedHandler 의 handler 가 작동해
 todo : 스프링 시큐리티 필터의 오류처리는  아래 순서로 작동하게되는데
 todo : JwtAccessDeniedHandler-> JwtAuthenticationEntryPoint -> AppExceptionController
 todo : JwtAuthenticationEntryPoint 에 도달해서 commence 메서드가 작동하면 이 녀석이 항상
 todo : AppExceptionController 의 handleAuthenticationException 를 작동시키는 것이 문제야
 todo : JwtAccessDeniedHandler 에서  JwtAuthenticationEntryPoint 에게 예외를 던졌을때
 todo : authException 으로만 던지는 것이 아니라.
 todo : accessDeniedException 으로 던져야할것같아

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto errorResponse
                = new ErrorResponseDto(HttpStatus.FORBIDDEN.toString(), "[Access Denied] :  API에 접근하는 데 필요한 권한이 없습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
*/