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
        try {
            LoginResponseDto loginResponse = authService.authenticate(loginRequestDto);
            ApiResponseDto<LoginResponseDto> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "로그인을 성공 하였습니다.", loginResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        catch (AuthenticationException e) {

            ErrorResponseDto errorResponse
                    = new ErrorResponseDto(HttpStatus.PRECONDITION_FAILED.toString(), "ID 혹은 PW를 확인해 주세요.");
            return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);}
        }
    }

