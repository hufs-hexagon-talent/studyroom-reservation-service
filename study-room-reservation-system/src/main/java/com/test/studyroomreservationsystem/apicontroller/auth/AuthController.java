package com.test.studyroomreservationsystem.apicontroller.auth;


import com.test.studyroomreservationsystem.dto.ApiResponse;
import com.test.studyroomreservationsystem.dto.ErrorResponseDto;
import com.test.studyroomreservationsystem.dto.auth.LoginRequestDto;
import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
import com.test.studyroomreservationsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(HttpStatus.OK.toString(), "로그인을 성공 하였습니다.", loginResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (AuthenticationServiceException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED.toString(), "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
        }
        catch (AuthenticationException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.toString(), "Error parsing login request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
        }
    }
}
