package com.test.studyroomreservationsystem.apicontroller.admin;


import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.dto.partition.PartitionRequestDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionResponseDto;
import com.test.studyroomreservationsystem.dto.partition.PartitionUpdateRequestDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.service.RoomPartitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RoomPartition", description = "방 정보 관련 API")
@RestController
@RequestMapping("/partitions")
public class AdminPartitionController {
    private final RoomPartitionService partitionService;

    @Autowired
    public AdminPartitionController(RoomPartitionService partitionService) {
        this.partitionService = partitionService;
    }

    @Operation(summary = "✅[관리자] Room의 Partition 생성",
            description = "partition 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping("/partition")
    public ResponseEntity<ApiResponseDto<PartitionResponseDto>> createPartition(@RequestBody PartitionRequestDto requestDto) {
        RoomPartition createdRoomPartition = partitionService.createRoomPartition(requestDto);
        PartitionResponseDto responseDto = partitionService.dtoFrom(createdRoomPartition);

        ApiResponseDto<PartitionResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "✅[관리자] partition 조회",
            description = "partition id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{partitionId}")
    public ResponseEntity<ApiResponseDto<PartitionResponseDto>> getPartitionById(@PathVariable Long partitionId) {
        RoomPartition foundPartition = partitionService.findRoomPartitionById(partitionId);
        PartitionResponseDto responseDto = partitionService.dtoFrom(foundPartition);
        ApiResponseDto<PartitionResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "✅[관리자] roomID로 partition들 조회",
            description = "room id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<PartitionResponseDto>>> getPartitionsByRoomId(@PathVariable Long roomId) {
        List<RoomPartition> partitions = partitionService.findRoomPartitionsByRoomId(roomId);
        List<PartitionResponseDto> partitionsDto = partitions.stream()
                .map(partitionService::dtoFrom)
                .toList();
        ApiResponseListDto<PartitionResponseDto> wrapped = new ApiResponseListDto<>(partitionsDto);
        ApiResponseDto<ApiResponseListDto<PartitionResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "✅[관리자] 모든 partition 조회",
            description = "모든 partition 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<PartitionResponseDto>>> getAllPartitions() {
        List<PartitionResponseDto> partitions = partitionService.findAllRoomPartition()
                .stream()
                .map(partitionService::dtoFrom)
                .toList();

        ApiResponseListDto<PartitionResponseDto> wrapped = new ApiResponseListDto<>(partitions);
        ApiResponseDto<ApiResponseListDto<PartitionResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @Operation(summary = "✅[관리자] partition 정보 수정",
            description = "해당 partition id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/{partitionId}")
    public ResponseEntity<ApiResponseDto<PartitionResponseDto>> updatePartition(@PathVariable Long partitionId,
                                              @RequestBody PartitionUpdateRequestDto requestDto) {
        RoomPartition updatedPartition = partitionService.updateRoomPartition(partitionId, requestDto);
        PartitionResponseDto partitionDto = partitionService.dtoFrom(updatedPartition);
        ApiResponseDto<PartitionResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 업데이트 되었습니다.", partitionDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "✅[관리자] partition 삭제",
            description = "해당 partition id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{partitionId}")
    public ResponseEntity<ApiResponseDto<Object> > deletePartition(@PathVariable Long partitionId) {
        partitionService.deletePartitionById(partitionId);
        ApiResponseDto<Object> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
