package com.test.studyroomreservationsystem.apicontroller.admin;

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

    @Operation(summary = "user 생성", description = "user 생성하는 API")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User createdUser = userService.createUser(userDto);
        UserDto user = userService.convertToDto(createdUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @Operation(summary = "user 조회", description = "user id로 조회 API")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        User foundUser = userService.findUserById(userId);
        UserDto user = userService.convertToDto(foundUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "모든 user 조회", description = "모든 user 조회 API")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers()
                .stream().map(userService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @Operation(summary = "user 정보 업데이트", description = "해당 user id의 정보 업데이트 API")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        User updatedUser = userService.updateUser(userId, userUpdateDto);
        UserDto user = userService.convertToDto(updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "user 삭제", description = "해당 user id의 정보 삭제 API")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
