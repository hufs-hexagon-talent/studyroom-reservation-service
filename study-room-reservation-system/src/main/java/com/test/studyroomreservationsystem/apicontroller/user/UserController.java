package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.security.dto.SingUpRequestDto;
import com.test.studyroomreservationsystem.security.dto.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "✅ 회원가입", description = "아이디, 비밀번호, 학번, 이름")
    @PostMapping("/sign-up")
    public HttpStatus signUpProcess(@RequestBody SingUpRequestDto singUpRequestDto) {
        userService.signUpProcess(singUpRequestDto);
        return HttpStatus.CREATED;
    }
    @Operation(summary = "❌ 자신의 정보 조회", description = "본인 정보 조회 API")
    // todo 수정 예정
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        User foundUser = userService.findUserById(userId);
        UserDto user = userService.dtoFrom(foundUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "❌ 자신의 정보 업데이트", description = "본인 정보 업데이트 API")
    // todo 수정 예정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        User updatedUser = userService.updateUser(userId, userUpdateDto);
        UserDto user = userService.dtoFrom(updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
