package com.test.studyroomreservationsystem.apicontroller.auth;


import com.test.studyroomreservationsystem.dto.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.ErrorResponseDto;
import com.test.studyroomreservationsystem.dto.auth.LoginRequestDto;
import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
import com.test.studyroomreservationsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "✅ 로그인 / 엑세스 토큰 발급", description = "로그인 (JWT Access-Token) ")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        // todo : 이미 아이디 존재하는 경우 예외 핸들링
        // todo : 아이디는 일치하나 비밀번호가 틀린 경우 예외 핸들링
        // todo : 비밀번호 재확인 추가

        try {
            LoginResponseDto loginResponse = authService.authenticate(loginRequestDto);
            ApiResponseDto<LoginResponseDto> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "로그인을 성공 하였습니다.", loginResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (AuthenticationServiceException e) {

            ErrorResponseDto errorResponse
                    = new ErrorResponseDto(HttpStatus.UNAUTHORIZED.toString(), "Invalid credentials");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);}
        catch (AuthenticationException e) {

            ErrorResponseDto errorResponse
                    = new ErrorResponseDto(HttpStatus.PRECONDITION_FAILED.toString(), "Error parsing login request");
            return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);}
        }
    }

