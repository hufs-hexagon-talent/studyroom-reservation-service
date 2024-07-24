package com.test.studyroomreservationsystem.apicontroller.admin;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.operationpolicy.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.operationpolicy.RoomOperationPolicyUpdateDto;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponseDto<RoomOperationPolicyDto>> createPolicy(@RequestBody RoomOperationPolicyDto policyDto) {

        RoomOperationPolicy createdPolicy = roomOperationPolicyService.createPolicy(policyDto);
        RoomOperationPolicyDto policy = roomOperationPolicyService.dtoFrom(createdPolicy);
        ApiResponseDto<RoomOperationPolicyDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", policy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "✅[관리자] RoomOperationPolicy 조회",
            description = "RoomOperationPolicy id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{roomOperationPolicyId}")
    public ResponseEntity<ApiResponseDto<RoomOperationPolicyDto>> getPolicy(@PathVariable Long roomOperationPolicyId) {
        RoomOperationPolicy foundPolicy = roomOperationPolicyService.findPolicyById(roomOperationPolicyId);// dto 로 전환
        RoomOperationPolicyDto policy = roomOperationPolicyService.dtoFrom(foundPolicy);
        ApiResponseDto<RoomOperationPolicyDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", policy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "✅[관리자] 모든 RoomOperationPolicy 조회",
            description = "모든 RoomOperationPolicy 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<RoomOperationPolicyDto>>> getAllPolices() {
        List<RoomOperationPolicyDto> policies = roomOperationPolicyService.findAllPolicies()
                .stream()
                .map(roomOperationPolicyService::dtoFrom)
                .toList();

        ApiResponseListDto<RoomOperationPolicyDto> wrapped = new ApiResponseListDto<>(policies);
        ApiResponseDto<ApiResponseListDto<RoomOperationPolicyDto>> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "❌[관리자] RoomOperationPolicy 정보 업데이트",
            description = "해당 RoomOperationPolicy id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/policy")
    public ResponseEntity<RoomOperationPolicyDto> updatePolicy(@PathVariable Long roomOperationPolicyId,
                                                                     @RequestBody RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy updatedPolicy = roomOperationPolicyService.updatePolicy(roomOperationPolicyId, policyDto);
        RoomOperationPolicyDto policy = roomOperationPolicyService.dtoFrom(updatedPolicy);

        return new ResponseEntity<>(policy, HttpStatus.OK);
    }

    @Operation(summary = "✅[관리자] RoomOperationPolicy 삭제",
            description = "해당 RoomOperationPolicy id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{roomOperationPolicyId}")
    public ResponseEntity<Object> deletePolicy(@PathVariable Long roomOperationPolicyId) {
        roomOperationPolicyService.deletePolicy(roomOperationPolicyId);
        ApiResponseDto<Object> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
