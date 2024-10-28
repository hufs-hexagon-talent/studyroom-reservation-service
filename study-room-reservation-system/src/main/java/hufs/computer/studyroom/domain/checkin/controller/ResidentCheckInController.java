package hufs.computer.studyroom.domain.checkin.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.checkin.dto.response.CheckInResponse;
import hufs.computer.studyroom.domain.checkin.service.CheckInService;
import hufs.computer.studyroom.domain.checkin.dto.request.CheckInRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Check-In", description = "예약한 정보를 방문 처리 하는 API")
@RestController
@RequestMapping("/check-in")
@RequiredArgsConstructor
public class ResidentCheckInController {
    private final CheckInService checkInService;

    @Operation(summary = "❌[멀티지기] 예약한 유저 Check-In",
            description = "예약시간에 근접한 유저의 방문 처리",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping
    public ResponseEntity<SuccessResponse<CheckInResponse>> verifyCheckInReservation(
            @Valid @RequestBody CheckInRequest request) {

        var result = checkInService.verifyCheckIn(request);
        return ResponseFactory.success(result);
    }
}
