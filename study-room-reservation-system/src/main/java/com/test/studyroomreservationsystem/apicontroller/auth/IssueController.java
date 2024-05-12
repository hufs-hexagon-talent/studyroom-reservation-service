package com.test.studyroomreservationsystem.apicontroller.auth;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.security.dto.UserDto;
import com.test.studyroomreservationsystem.security.dto.LoginRequestDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@Tag(name = "Auth", description = "인증 관련")
@RestController
public class IssueController {
    public final UserService userService;
    public IssueController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "✅ 로그인 / 엑세스 토큰 발급", description = "로그인 (JWT Access-Token) ")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.findByUsername(loginRequestDto.getUsername());
        UserDto userDto = userService.dtoFrom(user);
        Map<String, Object> result = new HashMap<>();
        result.put("data", userDto);
        return ResponseEntity.ok(result);
    }
}
