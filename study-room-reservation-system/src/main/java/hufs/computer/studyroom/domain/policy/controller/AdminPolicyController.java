package hufs.computer.studyroom.domain.policy.controller;
import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.ExistPolicy;
import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponse;
import hufs.computer.studyroom.domain.policy.dto.response.OperationPolicyInfoResponses;
import hufs.computer.studyroom.domain.policy.dto.request.ModifyOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.service.PolicyCommandService;
import hufs.computer.studyroom.domain.policy.service.PolicyQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RoomOperationPolicy", description = "Room 운영 정책 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/policies")
@Validated
public class AdminPolicyController {
    private final PolicyCommandService policyCommandService;
    private final PolicyQueryService policyQueryService;

    @Operation(summary = "❌[관리자] RoomOperationPolicy 생성",
            description = "RoomOperationPolicy를 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/policy")
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponse>> createPolicy(@RequestBody CreateOperationPolicyRequest policyDto) {
        var result = policyCommandService.createPolicy(policyDto);
        return ResponseFactory.created(result);
    }


    @Operation(summary = "❌[관리자] RoomOperationPolicy 조회",
            description = "RoomOperationPolicy id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/{roomOperationPolicyId}")
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponse>> getPolicy(@ExistPolicy @PathVariable Long roomOperationPolicyId) {
        var result = policyQueryService.findPolicyById(roomOperationPolicyId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "❌[관리자] 모든 RoomOperationPolicy 조회",
            description = "모든 RoomOperationPolicy 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponses>> getAllPolices() {
        var result = policyQueryService.findAllPolicies();
        return ResponseFactory.success(result);
    }

    @Operation(summary = "❌[관리자] RoomOperationPolicy 정보 업데이트",
            description = "해당 RoomOperationPolicy id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/policy/{roomOperationPolicyId}")
    public ResponseEntity<SuccessResponse<OperationPolicyInfoResponse>> updatePolicy(
            @ExistPolicy @PathVariable Long roomOperationPolicyId,
            @RequestBody ModifyOperationPolicyRequest requestDto) {

        var result = policyCommandService.updatePolicy(roomOperationPolicyId, requestDto);

        return ResponseFactory.modified(result);
    }

    @Operation(summary = "❌[관리자] RoomOperationPolicy 삭제",
            description = "해당 RoomOperationPolicy id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")})
    @DeleteMapping("/{roomOperationPolicyId}")
    public ResponseEntity<SuccessResponse<Void>> deletePolicy(@ExistPolicy @PathVariable Long roomOperationPolicyId) {
        policyCommandService.deletePolicy(roomOperationPolicyId);
        return ResponseFactory.deleted();
    }

}
