package com.test.studyroomreservationsystem.apicontroller.resident;

import com.test.studyroomreservationsystem.dto.CheckInResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.CheckInReservationDto;
import com.test.studyroomreservationsystem.dto.CheckInRequestDto;
import com.test.studyroomreservationsystem.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Check-In", description = "예약한 정보를 방문 처리 하는 API")
@RestController
@RequestMapping("/check-in")
public class ResidentCheckInController {
    private final CheckInService checkInService;

    public ResidentCheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @Operation(summary = "✅[멀티지기] 예약한 유저 Check-In",
            description = "예약시간에 근접한 유저의 방문 처리",
            security = {@SecurityRequirement(name = "JWT")}
    )

    @PostMapping
    public ResponseEntity<ApiResponseDto<CheckInResponseDto>> verifyCheckInReservation(@RequestBody CheckInRequestDto requestDto) {
        List<CheckInReservationDto> checkInReservations
                = checkInService.verifyCheckIn(requestDto.getVerificationCode(), requestDto.getRoomId());
        CheckInResponseDto responseDto
                = new CheckInResponseDto(checkInReservations);
        ApiResponseDto<CheckInResponseDto> response = new ApiResponseDto<>(HttpStatus.OK.toString(),
                "정상적으로 검증 되었습니다.", responseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
