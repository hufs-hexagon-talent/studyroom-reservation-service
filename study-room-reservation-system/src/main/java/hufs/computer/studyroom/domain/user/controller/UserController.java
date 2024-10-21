package hufs.computer.studyroom.domain.user.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.user.dto.request.ModifyPasswordRequest;
import hufs.computer.studyroom.domain.user.dto.request.ResetPasswordRequest;
import hufs.computer.studyroom.domain.user.dto.request.SignUpRequest;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.service.UserCommandService;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import hufs.computer.studyroom.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Operation(summary = "✅ 회원가입",
            description = "아이디, 비밀번호, 학번, 이름")
    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> signUpProcess(@Valid @RequestBody SignUpRequest request) {
        var result = userCommandService.signUpProcess(request);
        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅ 자신의 정보 조회",
            description = "본인 정보 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails currentUser) {
        var result = userQueryService.findUserById(currentUser.getUser().getUserId());
        return ResponseFactory.success(result);
    }


    @Operation(summary = "✅로그인 후, 자신의 비밀번호 수정",
            description = "로그인 후, 본인 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PutMapping("/me/password")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> updateUser(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                          @RequestBody ModifyPasswordRequest request) {
        User user = currentUser.getUser();
        var result = userCommandService.resetUserPasswordWithOldPassword(user.getUserId(), request);
        return ResponseFactory.modified(result);
    }

    @Operation(summary = "✅로그인 X, 자신의 비밀번호 재설정",
            description = "JWT 토큰과 새로운 비밀번호를 사용하여 비밀번호를 재설정하는 API")
    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> resetPassword(@RequestBody ResetPasswordRequest request) {
        var result = userCommandService.resetUserPasswordWithToken(request);// 비밀번호 업데이트
        return ResponseFactory.modified(result);
    }
}
