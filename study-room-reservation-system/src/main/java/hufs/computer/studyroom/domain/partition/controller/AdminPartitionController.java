package hufs.computer.studyroom.domain.partition.controller;


import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.partition.dto.request.CreatePartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.request.ModifyPartitionRequest;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponse;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.service.RoomPartitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RoomPartition", description = "방 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/partitions")
public class AdminPartitionController {
    private final RoomPartitionService partitionService;


    @Operation(summary = "✅[관리자] Room의 Partition 생성",
            description = "partition 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping("/partition")
    public ResponseEntity<SuccessResponse<PartitionInfoResponse>> createPartition(@RequestBody CreatePartitionRequest requestDto) {
        var result = partitionService.createRoomPartition(requestDto);
        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅[관리자] partition 조회",
            description = "partition id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{partitionId}")
    public ResponseEntity<SuccessResponse<PartitionInfoResponse>> getPartitionById(@PathVariable Long partitionId) {
        var result = partitionService.findRoomPartitionById(partitionId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 모든 partition 조회",
            description = "모든 partition 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<SuccessResponse<PartitionInfoResponses>> getAllPartitions() {
        var result = partitionService.findAll();
        return ResponseFactory.success(result);

    }
    @Operation(summary = "✅[관리자] partition 정보 수정",
            description = "해당 partition id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/{partitionId}")
    public ResponseEntity<SuccessResponse<PartitionInfoResponse>> updatePartition(@PathVariable Long partitionId,
                                                @RequestBody ModifyPartitionRequest requestDto) {
        var result = partitionService.modifyPartition(partitionId, requestDto);
        return ResponseFactory.modified(result);
    }


    @Operation(summary = "✅[관리자] partition 삭제",
            description = "해당 partition id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{partitionId}")
    public ResponseEntity<SuccessResponse<Void>> deletePartition(@PathVariable Long partitionId) {
        partitionService.deletePartitionById(partitionId);
        return ResponseFactory.deleted();
    }
}
