package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ErrorResponseDto;
import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.dto.user.SingUpRequestDto;
import com.test.studyroomreservationsystem.dto.user.UserInfoResponseDto;
import com.test.studyroomreservationsystem.dto.user.UserInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "✅ 회원가입",
            description = "아이디, 비밀번호, 학번, 이름",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponseDto.class)
                            )),
                    @ApiResponse(responseCode = "409",
                            description = "로그인 ID, 학번이 이미 존재",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            ))
                    }
            )

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponseDto<UserInfoResponseDto>> signUpProcess(@RequestBody SingUpRequestDto singUpRequestDto) {
        User createdUser = userService.signUpProcess(singUpRequestDto);
        UserInfoResponseDto user = userService.dtoFrom(createdUser);

        ApiResponseDto<UserInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", user);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "✅ 자신의 정보 조회",
            description = "본인 정보 조회 API",
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
                            ))

            }
    )

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal CustomUserDetails currentUser) {

        User foundUser = userService.findUserById(currentUser.getUser().getUserId());
        UserInfoResponseDto user = userService.dtoFrom(foundUser);
        ApiResponseDto<UserInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "✅ 자신의 비밀번호 수정",
            description = "본인 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto<UserInfoResponseDto>> updateUser(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                          @RequestBody UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = currentUser.getUser();
        User updatedUser = userService.updateUser(user.getUserId(), userInfoUpdateRequestDto);
        UserInfoResponseDto userDto = userService.dtoFrom(updatedUser);
        ApiResponseDto<UserInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 변경 되었습니다.", userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
