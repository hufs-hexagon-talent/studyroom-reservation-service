package hufs.computer.studyroom.domain.auth.controller;


import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.auth.dto.request.LoginRequest;
import hufs.computer.studyroom.domain.auth.dto.request.RefreshRequest;
import hufs.computer.studyroom.domain.auth.dto.response.LoginResponse;
import hufs.computer.studyroom.domain.auth.dto.response.RefreshResponse;
import hufs.computer.studyroom.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "❌ 로그인 / 엑세스 토큰 발급", description = "로그인 (JWT Access-Token) ")
    public ResponseEntity<SuccessResponse<LoginResponse>> accessToken(@RequestBody LoginRequest request) {
        var result = authService.issueToken(request);
        return ResponseFactory.success(result);
    }


    @PostMapping("/auth/refresh")
    @Operation(summary = "❌ accessToken 토큰 재발급  ", description = "리프레시 토큰으로 엑세스 토큰 재발급")
    public ResponseEntity<SuccessResponse<RefreshResponse>> reissueToken(
            @RequestBody RefreshRequest request
//            HttpServletRequest request
    ){
//        String refreshToken = cookieService.getRefreshToken(request);
        String refreshToken = request.refreshToken();
        var result = authService.reissueToken(refreshToken);
        return ResponseFactory.success(result);
    }

}


