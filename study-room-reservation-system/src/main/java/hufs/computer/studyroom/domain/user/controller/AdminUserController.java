package hufs.computer.studyroom.domain.user.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import hufs.computer.studyroom.domain.user.dto.request.ModifyUserInfoRequest;
import hufs.computer.studyroom.domain.user.dto.request.SignUpBulkRequest;
import hufs.computer.studyroom.domain.user.dto.response.UserBlockedInfoResponses;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.service.UserCommandService;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    // request param -> json request
    @Operation(summary = "✅ [관리자] 특정 회원 정보 조회",
            description = "username, password, isAdmin, name, serial 반환",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserById(
            @ExistUser @PathVariable Long userId) {
        var result = userQueryService.findUserById(userId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자] 모든 회원 정보 조회",
            description = "모든 user 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping
    public ResponseEntity<SuccessResponse<UserInfoResponses>> getAllUsers() {
        var result = userQueryService.findAllUsers();
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자] 여러 회원 등록",
            description = "user 등록 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/sign-up/bulk")
    public ResponseEntity<SuccessResponse<UserInfoResponses>> signUpUsers(
            @Valid @RequestBody SignUpBulkRequest bulkRequest) {
        var result = userCommandService.signUpUsers(bulkRequest);
        return ResponseFactory.created(result);
    }

    // request param -> json request
    @Operation(summary = "✅ [관리자] 특정 회원 삭제",
            description = "해당 user id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")})
    @DeleteMapping("/{userId}")
    public ResponseEntity<SuccessResponse<Void>> deleteUser(
            @ExistUser @PathVariable Long userId) {
        userCommandService.deleteUser(userId);
        return ResponseFactory.deleted();
    }

    @Operation(summary = "✅[관리자] 블락 당한 사용자들 조회",
            description = "관리용 예약 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/blocked")
    public ResponseEntity<SuccessResponse<UserBlockedInfoResponses>> getBlockedUserReservationInfo() {
        var result = userQueryService.findBlockedUser();

        return ResponseFactory.success(result);
    }
    @Operation(summary = "✅ [관리자] 특정 회원 정보 수정",
            description = "해당 user id의 정보를 수정 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/{userId}")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> updateUserInfo(
            @ExistUser @PathVariable Long userId,
            @Valid @RequestBody ModifyUserInfoRequest request) {
        var result = userCommandService.updateUserInfo(userId,request);
        return ResponseFactory.modified(result);
    }
}
