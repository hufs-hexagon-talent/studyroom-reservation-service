package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.dto.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.CheckInResponseDto;
import com.test.studyroomreservationsystem.dto.CheckInRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check-in")
public class AdminCheckInController {
    @PostMapping
    private ResponseEntity<ApiResponseDto<CheckInResponseDto>> verifyCheckIn(@RequestBody CheckInRequestDto requestDto) {

        return null;
    }
}
