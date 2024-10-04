package hufs.computer.studyroom.domain.user.controller;

import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.dto.SingUpRequestDto;
import hufs.computer.studyroom.domain.user.dto.UserInfoUpdateRequestDto;
import hufs.computer.studyroom.common.util.ApiResponseDto;
import hufs.computer.studyroom.common.util.ApiResponseListDto;
import hufs.computer.studyroom.common.util.ErrorResponseDto;
import hufs.computer.studyroom.domain.user.dto.UserInfoResponseDto;
import hufs.computer.studyroom.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")

public class AdminUserController {
    private final UserService userService;
    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // request param -> json request
    @Operation(summary = "✅ [관리자] 특정 회원 정보 조회",
            description = "username, password, isAdmin, name, serial 반환",
            security = {@SecurityRequirement(name = "JWT")},
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserInfoResponseDto.class)
                            )),
                    @ApiResponse(responseCode = "401",
                            description = "인증 요구",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )),
                    @ApiResponse(responseCode = "403",
                            description = "권한 부족",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            ))

            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserInfoResponseDto>> getUserById(@PathVariable Long userId) {
        User foundUser = userService.findUserById(userId);
        UserInfoResponseDto user = userService.dtoFrom(foundUser);
        ApiResponseDto<UserInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "✅ [관리자] 모든 회원 정보 조회",
            description = "모든 user 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<UserInfoResponseDto>>> getAllUsers() {
        List<UserInfoResponseDto> users = userService.findAllUsers()
                .stream().map(userService::dtoFrom)
                .toList();

        ApiResponseListDto<UserInfoResponseDto> wrapped = new ApiResponseListDto<>(users);

        ApiResponseDto<ApiResponseListDto<UserInfoResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "✅ [관리자] 회원 등록",
            description = "user 등록 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<UserInfoResponseDto>>> signUpUsers(@RequestBody List<SingUpRequestDto> singUpRequestDtos) {
        List<UserInfoResponseDto> users = userService.signUpUsers(singUpRequestDtos)
                        .stream().map(userService::dtoFrom)
                        .toList();

        ApiResponseListDto<UserInfoResponseDto> wrapped = new ApiResponseListDto<>(users);

        ApiResponseDto<ApiResponseListDto<UserInfoResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 등록 되었습니다.", wrapped);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // request param -> json request
    @Operation(summary = "✅ [관리자] 특정 회원 삭제",
            description = "해당 user id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<Objects>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        ApiResponseDto<Objects> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @Operation(summary = "✅ [관리자] 특정 회원 정보 수정",
            description = "해당 user id의 정보를 수정 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserInfoResponseDto>> updateUserInfo(@PathVariable Long userId,
                                                                              @RequestBody UserInfoUpdateRequestDto requestDto) {
        User user = userService.updateUserInfo(userId, requestDto);
        UserInfoResponseDto userInfoDto = userService.dtoFrom(user);
        ApiResponseDto<UserInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 수정 되었습니다.", userInfoDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
