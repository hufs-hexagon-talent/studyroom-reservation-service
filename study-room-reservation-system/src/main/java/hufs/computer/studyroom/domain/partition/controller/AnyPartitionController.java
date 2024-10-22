package hufs.computer.studyroom.domain.partition.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionPolicyResponses;
import hufs.computer.studyroom.domain.partition.service.PartitionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "RoomPartition", description = "파티션 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/partitions")
public class AnyPartitionController {

    private final PartitionQueryService partitionQueryService;

    @Operation(summary = "✅ 해당 날짜 모든 파티션 운영시간 확인 ",
            description = "날짜를 받으면 모든 룸의 정책에 따른 파티션 운영시간을 확인",
            security = {})
    @GetMapping("/policy/by-date")
    ResponseEntity<SuccessResponse<PartitionPolicyResponses>> getPartitionPolicyByRoomAndDate(@RequestParam("date") LocalDate date) {
        var result = partitionQueryService.getPartitionsPolicyByDate(date);
        return ResponseFactory.success(result);
    }
}
