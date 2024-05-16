package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserInfoResponseDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/admin/users")

public class AdminUserController {
    private final UserService userService;
    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "✅ 특정 회원 정보 조회",
            description = "username, password, isAdmin, name, serial 반환",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> getUserById(@PathVariable Long userId) {
        User foundUser = userService.findUserById(userId);
        UserInfoResponseDto user = userService.dtoFrom(foundUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "✅ 모든 회원 정보 조회",
            description = "모든 user 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<List<UserInfoResponseDto>> getAllUsers() {
        List<UserInfoResponseDto> users = userService.findAllUsers()
                .stream().map(userService::dtoFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @Operation(summary = "✅ 특정 회원 삭제",
            description = "해당 user id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
