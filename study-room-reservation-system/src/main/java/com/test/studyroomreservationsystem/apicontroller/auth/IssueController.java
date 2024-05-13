package com.test.studyroomreservationsystem.apicontroller.auth;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.security.dto.UserDto;
import com.test.studyroomreservationsystem.security.dto.LoginRequestDto;
import com.test.studyroomreservationsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@Tag(name = "Auth", description = "인증 관련")
@RestController
@Slf4j
public class IssueController {
    public final UserService userService;
    public IssueController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @Operation(summary = "✅ 로그인 / 엑세스 토큰 발급", description = "로그인 (JWT Access-Token) ")
    public String userLogin(@RequestBody LoginRequestDto loginRequestDto) {
        log.trace("[정상작동]login 컨트롤러가 작동이 시작");
        log.trace("[정상작동]login 컨트롤러가 작동이 종료.");
        return "Swagger 문서화 용 실제로 해당 핸들러는 작동 하지 않음 ";
    }
}



