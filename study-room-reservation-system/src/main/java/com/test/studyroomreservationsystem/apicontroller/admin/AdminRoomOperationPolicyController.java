package com.test.studyroomreservationsystem.apicontroller.admin;
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
@RequestMapping("/admin/policies")
public class AdminRoomOperationPolicyController {
    private final RoomOperationPolicyService roomOperationPolicyService;
    @Autowired
    public AdminRoomOperationPolicyController(RoomOperationPolicyService roomOperationPolicyService) {
        this.roomOperationPolicyService = roomOperationPolicyService;
    }
    @Operation(summary = "RoomOperationPolicy 생성", description = "RoomOperationPolicy를 생성하는 API")
    @PostMapping
    public ResponseEntity<RoomOperationPolicyDto> createPolicy(@RequestBody RoomOperationPolicyDto policyDto) {

        RoomOperationPolicy createdPolicy = roomOperationPolicyService.createPolicy(policyDto);
        RoomOperationPolicyDto policy = roomOperationPolicyService.convertToDto(createdPolicy);

        return new ResponseEntity<>(policy, HttpStatus.CREATED);
    }
    @Operation(summary = "RoomOperationPolicy 조회", description = "RoomOperationPolicy id로 조회 API")
    @GetMapping("/{roomOperationPolicyId}")
    public ResponseEntity<RoomOperationPolicyDto> getPolicy(@PathVariable Long roomOperationPolicyId) {
        RoomOperationPolicy foundPolicy = roomOperationPolicyService.findPolicyById(roomOperationPolicyId);// dto 로 전환
        RoomOperationPolicyDto policy = roomOperationPolicyService.convertToDto(foundPolicy);
        return new ResponseEntity<>(policy, HttpStatus.OK);
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
        RoomOperationPolicy updatedPolicy = roomOperationPolicyService.updatePolicy(roomOperationPolicyId, policyDto);
        RoomOperationPolicyUpdateDto policy = roomOperationPolicyService.convertToUpdateDto(updatedPolicy);

        return new ResponseEntity<>(policy, HttpStatus.OK);
    }
    @Operation(summary = "RoomOperationPolicy 삭제", description = "해당 RoomOperationPolicy id의 정보 삭제 API")
    @DeleteMapping("/{roomOperationPolicyId}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long roomOperationPolicyId) {
        roomOperationPolicyService.deletePolicy(roomOperationPolicyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
