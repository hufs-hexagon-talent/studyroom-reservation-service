package hufs.computer.studyroom.apicontroller.any;

import hufs.computer.studyroom.dto.partition.PartitionPolicyResponseDto;
import hufs.computer.studyroom.dto.util.ApiResponseDto;
import hufs.computer.studyroom.dto.util.ApiResponseListDto;
import hufs.computer.studyroom.service.RoomPartitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "RoomPartition", description = "파티션 정보 관련 API")
@RestController
@RequestMapping("/partitions")
public class AnyPartitionController {
    @Autowired
    private RoomPartitionService roomPartitionService;


    @Operation(summary = "✅ 해당 날짜 모든 파티션 운영시간 확인 ",
            description = "날짜를 받으면 모든 룸의 정책에 따른 파티션 운영시간을 확인",
            security = {})
    @GetMapping("/policy/by-date")
    ResponseEntity<ApiResponseDto<ApiResponseListDto<PartitionPolicyResponseDto>>> getPartitionPolicyByRoomAndDate(@RequestParam("date") LocalDate date) {
        List<PartitionPolicyResponseDto> responseDtoList = roomPartitionService.getPartitionsPolicyByDate(date);
        ApiResponseListDto<PartitionPolicyResponseDto> wrapped = new ApiResponseListDto<>(responseDtoList);
        ApiResponseDto<ApiResponseListDto<PartitionPolicyResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
