package com.test.studyroomreservationsystem.apicontroller.auth;


import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ErrorResponseDto;
import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
import com.test.studyroomreservationsystem.dto.auth.RefreshRequestDto;
import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Tag(name = "Auth", description = "인증 관련")
@RestController
public class RefreshController {
    private final JWTUtil jwtUtil;
    private final String jwtRefreshCategory;
    private final String jwtAccessCategory;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    public RefreshController(JWTUtil jwtUtil,
                             @Value("${spring.jwt.refresh.category}") String jwtRefreshCategory,
                             @Value("${spring.jwt.access.category}") String jwtAccessCategory,
                             @Value("${spring.jwt.access.expiration}") Long accessTokenExpiration,
                             @Value("${spring.jwt.refresh.expiration}") Long refreshTokenExpiration
    ) {
        this.jwtUtil = jwtUtil;
        this.jwtRefreshCategory = jwtRefreshCategory;
        this.jwtAccessCategory = jwtAccessCategory;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;

    }

    @PostMapping("/auth/refresh")
    @Operation(summary = "✅ 엑세스 토큰 재발급  ", description = "리프레시 토큰으로 엑세스 토큰 재발급")
    public ResponseEntity<?> reissueProcess(@RequestBody RefreshRequestDto requestDto) {

        String refresh = requestDto.getRefresh_token();

        if (refresh == null) {
            //response status code
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.BAD_REQUEST.getReasonPhrase(), "refresh_token 이 null 입니다.");
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.BAD_REQUEST.getReasonPhrase(), "refresh_token 이 만료 되었습니다.");
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(jwtRefreshCategory)) {
            //response status code
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.BAD_REQUEST.getReasonPhrase(), "refresh_token 이 아닙니다.");
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }

        // login 성공 핸들러가 응답하는 것과 동일하게...

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String accessToken = jwtUtil.createAccessJwt(jwtAccessCategory, username, role, accessTokenExpiration * 1000); // 밀리초 -> 초
        String refreshToken = jwtUtil.createRefreshJwt(jwtRefreshCategory, username, refreshTokenExpiration * 1000);
        LoginResponseDto loginResponse = new LoginResponseDto(accessToken, refreshToken);
        ApiResponseDto<LoginResponseDto> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 발급 되었습니다.", loginResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}