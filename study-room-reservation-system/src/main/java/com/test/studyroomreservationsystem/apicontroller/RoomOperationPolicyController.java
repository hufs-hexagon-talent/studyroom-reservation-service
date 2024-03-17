package com.test.studyroomreservationsystem.apicontroller;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicy.RoomOperationPolicyUpdateDto;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Tag(name = "RoomOperationPolicy", description = "Room 운영 정책 관련 API")
@RestController
@RequestMapping("/api/policies")
public class RoomOperationPolicyController {
    private final RoomOperationPolicyService roomOperationPolicyService;
    @Autowired
    public RoomOperationPolicyController(RoomOperationPolicyService roomOperationPolicyService) {
        this.roomOperationPolicyService = roomOperationPolicyService;
    }
    @Operation(summary = "RoomOperationPolicy 생성", description = "RoomOperationPolicy를 생성하는 API")
    @PostMapping
    public ResponseEntity<RoomOperationPolicyDto> createPolicy(@RequestBody RoomOperationPolicyDto policyDto) {

        RoomOperationPolicy policy = roomOperationPolicyService.createPolicy(policyDto);
        RoomOperationPolicyDto createdPolicy = roomOperationPolicyService.convertToDto(policy);

        return new ResponseEntity<>(createdPolicy, HttpStatus.CREATED);
    }
    @Operation(summary = "RoomOperationPolicy 조회", description = "RoomOperationPolicy id로 조회 API")
    @GetMapping("/{roomOperationPolicyId}")
    public ResponseEntity<RoomOperationPolicyDto> getPolicy(@PathVariable Long roomOperationPolicyId) {
        RoomOperationPolicy policy = roomOperationPolicyService.findPolicyById(roomOperationPolicyId);// dto 로 전환
        RoomOperationPolicyDto foundPolicy = roomOperationPolicyService.convertToDto(policy);
        return new ResponseEntity<>(foundPolicy, HttpStatus.OK);
    }
    @Operation(summary = "모든 RoomOperationPolicy 조회", description = "모든 RoomOperationPolicy 조회 API")
    @GetMapping
    public ResponseEntity<List<RoomOperationPolicyDto>> getAllPolices() {
        List<RoomOperationPolicyDto> policies = roomOperationPolicyService.getAllPolicies()
                .stream()
                .map(roomOperationPolicyService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(policies, HttpStatus.OK);
    }
    @Operation(summary = "RoomOperationPolicy 정보 업데이트", description = "해당 RoomOperationPolicy id의 정보 업데이트 API")
    @PutMapping("/{roomOperationPolicyId}")
    public ResponseEntity<RoomOperationPolicyUpdateDto> updatePolicy(@PathVariable Long roomOperationPolicyId,
                                                                     @RequestBody RoomOperationPolicyUpdateDto policyDto) {
        RoomOperationPolicy policy = roomOperationPolicyService.updatePolicy(roomOperationPolicyId, policyDto);
        RoomOperationPolicyUpdateDto updatedPolicy = roomOperationPolicyService.convertToUpdateDto(policy);

        return new ResponseEntity<>(updatedPolicy, HttpStatus.OK);
    }
    @Operation(summary = "RoomOperationPolicy 삭제", description = "해당 RoomOperationPolicy id의 정보 삭제 API")
    @DeleteMapping("/{roomOperationPolicyId}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long roomOperationPolicyId) {
        roomOperationPolicyService.deletePolicy(roomOperationPolicyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
