package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/user/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "user 조회", description = "본인 정보 조회 API")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        User foundUser = userService.findUserById(userId);
        UserDto user = userService.dtoFrom(foundUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "user 정보 업데이트", description = "본인 정보 업데이트 API")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        User updatedUser = userService.updateUser(userId, userUpdateDto);
        UserDto user = userService.dtoFrom(updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
