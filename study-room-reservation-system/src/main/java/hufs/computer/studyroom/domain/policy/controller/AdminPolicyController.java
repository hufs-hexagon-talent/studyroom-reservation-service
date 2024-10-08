package hufs.computer.studyroom.domain.policy.controller;
import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponses;
import hufs.computer.studyroom.domain.policy.dto.request.ModifyOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.service.RoomOperationPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RoomOperationPolicy", description = "Room 운영 정책 관련 API")
@RestController
@RequestMapping("/policies")
public class AdminPolicyController {
    private final RoomOperationPolicyService roomOperationPolicyService;
    @Autowired
    public AdminPolicyController(RoomOperationPolicyService roomOperationPolicyService) {
        this.roomOperationPolicyService = roomOperationPolicyService;
    }
    @Operation(summary = "✅[관리자] RoomOperationPolicy 생성",
            description = "RoomOperationPolicy를 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping("/policy")
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponse>> createPolicy(@RequestBody CreateOperationPolicyRequest policyDto) {
        var result = roomOperationPolicyService.createPolicy(policyDto);
        return ResponseFactory.created(result);
    }


    @Operation(summary = "✅[관리자] RoomOperationPolicy 조회",
            description = "RoomOperationPolicy id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{roomOperationPolicyId}")
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponse>> getPolicy(@PathVariable Long roomOperationPolicyId) {
        var result = roomOperationPolicyService.findPolicyById(roomOperationPolicyId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 모든 RoomOperationPolicy 조회",
            description = "모든 RoomOperationPolicy 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponses>> getAllPolices() {
        var result = roomOperationPolicyService.findAllPolicies();
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] RoomOperationPolicy 정보 업데이트",
            description = "해당 RoomOperationPolicy id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PatchMapping("/policy/{roomOperationPolicyId}")
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponse>> updatePolicy(
            @PathVariable Long roomOperationPolicyId,
            @RequestBody ModifyOperationPolicyRequest requestDto) {

        var result = roomOperationPolicyService.updatePolicy(roomOperationPolicyId, requestDto);

        return ResponseFactory.modified(result);
    }

    @Operation(summary = "✅[관리자] RoomOperationPolicy 삭제",
            description = "해당 RoomOperationPolicy id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{roomOperationPolicyId}")
    public ResponseEntity<SuccessResponse<Void>> deletePolicy(@PathVariable Long roomOperationPolicyId) {
        roomOperationPolicyService.deletePolicy(roomOperationPolicyId);
        return ResponseFactory.deleted();
    }

}
