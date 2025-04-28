package hufs.computer.studyroom.domain.user.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.util.excel.core.ExcelFile;
import hufs.computer.studyroom.common.util.excel.core.OneSheetExcelFile;
import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import hufs.computer.studyroom.domain.reservation.service.ReservationCommandService;
import hufs.computer.studyroom.domain.user.dto.excel.UserExportExcelDto;
import hufs.computer.studyroom.domain.user.dto.request.ModifyUserInfoRequest;
import hufs.computer.studyroom.domain.user.dto.request.SignUpBulkRequest;
import hufs.computer.studyroom.domain.user.dto.response.*;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.service.UserCommandService;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final ReservationCommandService reservationCommandService;

    // request param -> json request
    @Operation(summary = "✅ [관리자] 특정 회원 정보 조회",
            description = "특정 회원을 ID로 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/search/by-id/{userId}")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserById(
            @ExistUser @PathVariable Long userId) {
        var result = userQueryService.findUserById(userId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자] 특정 회원 정보 조회",
            description = "특정 회원을 학번으로 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/search/by-serial")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserBySerial(@RequestParam String serial) {
        var result = userQueryService.findUserBySerial(serial);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자] 특정 회원 정보 조회",
            description = "특정 회원을 이름으로 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/search/by-name")
    public ResponseEntity<SuccessResponse<UserInfoResponses>> getUserByName(@RequestParam String name) {
        var result = userQueryService.findUserByName(name);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자] 모든 회원 정보 조회",
            description = "모든 user 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/search")
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
            description = "관리용 유저 정보 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/blocked")
    public ResponseEntity<SuccessResponse<UserBlockedInfoResponses>> getBlockedUserReservationInfo() {
        var result = userQueryService.findBlockedUser();

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 블락 당한 사용자 블락 해제",
            description = "관리용 유저 정보 수정",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/unblocked/{userId}")
    public ResponseEntity<SuccessResponse<ReservationInfoResponses>> unBlockUserById(
            @ExistUser @PathVariable Long userId) {
        var result = reservationCommandService.updateNoShowReservationsToProcessed(userId);

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자] 특정 회원 정보 수정",
            description = "해당 user id의 정보를 수정 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/{userId}")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> updateUserInfo(
            @ExistUser @PathVariable Long userId,
            @Valid @RequestBody ModifyUserInfoRequest request) {
        var result = userCommandService.updateUserInfo(userId, request);
        return ResponseFactory.modified(result);
    }

    @Operation(summary = "✅[관리자] 사용자 통계 조회",
            description = "사용자 통계 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/statics")
    public ResponseEntity<SuccessResponse<UserStaticResponse>> getUserStatic() {
        var result = userQueryService.getUserStatics();

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 사용자 정보 Excel 내보내기",
            description = "사용자 정보 Excel 추출",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/export/excel")
    public ResponseEntity<SuccessResponse<Void>> exportExcel(
            @Parameter(in = ParameterIn.QUERY, description = "필터링 할 ServiceRole 목록(없으면 전체)")
            @RequestParam(value = "roles", required = false) List<ServiceRole> roles,

            HttpServletResponse response) throws IOException {

        List<UserExportExcelDto> data = userQueryService.getExcelDTOs(roles);
        ExcelFile excelFile = new OneSheetExcelFile<>(data, UserExportExcelDto.class);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        excelFile.write(response.getOutputStream());

        return ResponseFactory.success(null);
    }

    @Operation(summary = "✅[관리자] 사용자 역할 리스트 조회",
            description = "ServiceRole 전체 값을 반환",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/roles")
    public ResponseEntity<SuccessResponse<List<String>>> getRoles() {
        List<String> result = Arrays.stream(ServiceRole.values())
                .map(Enum::name)
                .toList();
        return ResponseFactory.success(result);
    }
}
