package com.test.studyroomreservationsystem.apicontroller.any;

import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.room.RoomResponseDto;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyScheduleService;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Room", description = "방 정보 관련 API")
@RestController
@Slf4j
@RequestMapping("/rooms")
public class AnyRoomController {
    @Autowired
    private RoomService roomService;


    @Operation(summary = "✅ 해당 날짜 모든 룸 운영시간 확인 ",
            description = "날짜를 받으면 모든 룸의 정책에 따른 운영시간을 확인",
            security = {})
    @GetMapping("/policy/by-date")
    ResponseEntity<ApiResponseDto<ApiResponseListDto<RoomResponseDto>>> getRoomPolicyByDate(@RequestParam("date") LocalDate date) {
        List<RoomResponseDto> responseDtoList = roomService.getRoomsPolicyByDate(date);

        ApiResponseListDto<RoomResponseDto> wrapped = new ApiResponseListDto<>(responseDtoList);
        ApiResponseDto<ApiResponseListDto<RoomResponseDto>> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
