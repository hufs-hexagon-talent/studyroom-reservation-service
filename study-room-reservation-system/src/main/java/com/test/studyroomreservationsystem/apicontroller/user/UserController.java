package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.security.dto.SingUpRequestDto;
import com.test.studyroomreservationsystem.security.dto.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "✅ 회원가입",
            description = "아이디, 비밀번호, 학번, 이름",
            security = {})
    @PostMapping("/sign-up")
    public HttpStatus signUpProcess(@RequestBody SingUpRequestDto singUpRequestDto) {
        userService.signUpProcess(singUpRequestDto);
        return HttpStatus.CREATED;
    }

    @Operation(summary = "✅ 자신의 정보 조회",
            description = "본인 정보 조회 API",
            security = {@SecurityRequirement(name = "JWT")})

    // todo 수정 예정,request시 헤더에 Authorization 에 access token 보내야함
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserById(@AuthenticationPrincipal CustomUserDetails currentUser) {
        log.trace("컨트롤러 진입 : [현재 유저 정보] = {}", currentUser.getUser().getUserId());
        log.trace("컨트롤러 진입 : [현재 유저 정보] = {}", currentUser.getUser().getName());
        log.trace("컨트롤러 진입 : [현재 유저 정보] = {}", currentUser.getUser().getSerial());
        log.trace("컨트롤러 진입 : [현재 유저 정보] = {}", currentUser.getAuthorities());
        log.trace("컨트롤러 진입 : [현재 유저 정보] = {}", currentUser.getUsername());

        User foundUser = userService.findUserById(currentUser.getUser().getUserId());
        UserDto user = userService.dtoFrom(foundUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Operation(summary = "❌ 자신의 정보 수정",
            description = "본인 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")})
    // todo 수정 예정
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        User updatedUser = userService.updateUser(userId, userUpdateDto);
        UserDto user = userService.dtoFrom(updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
