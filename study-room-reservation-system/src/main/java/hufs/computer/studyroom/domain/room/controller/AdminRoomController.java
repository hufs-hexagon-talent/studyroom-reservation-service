package hufs.computer.studyroom.domain.room.controller;


import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.room.dto.request.ModifyRoomRequest;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponse;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponses;
import hufs.computer.studyroom.domain.room.service.RoomCommandService;
import hufs.computer.studyroom.domain.room.service.RoomQueryService;
import hufs.computer.studyroom.domain.room.dto.request.CreateRoomRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Room", description = "방 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Validated
public class AdminRoomController {
    private final RoomQueryService roomQueryService;
    private final RoomCommandService roomCommandService;


    @Operation(summary = "✅[관리자] room 생성", description = "room 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/room")
    public ResponseEntity<SuccessResponse<RoomInfoResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        var result = roomCommandService.createRoom(request);
        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅[관리자] room 정보 수정",
            description = "해당 room id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/{roomId}")
    public ResponseEntity<SuccessResponse<RoomInfoResponse>> updateRoom(@ExistRoom @PathVariable Long roomId,
                                                                        @Valid @RequestBody ModifyRoomRequest request) {
        var result = roomCommandService.updateRoom(roomId, request);
        return ResponseFactory.modified(result);
    }

    @Operation(summary = "✅[관리자] room 삭제",
            description = "해당 room id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")})
    @DeleteMapping("/{roomId}")
    public ResponseEntity<SuccessResponse<Void>> deleteRoom(
            @ExistRoom @PathVariable Long roomId) {
        roomCommandService.deleteRoom(roomId);
        return ResponseFactory.deleted();
    }


    @Operation(summary = "✅[관리자] room 조회", description = "room id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/{roomId}")
    public ResponseEntity<SuccessResponse<RoomInfoResponse>> getRoomById(@ExistRoom @PathVariable Long roomId) {
        var result = roomQueryService.findRoomById(roomId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 모든 room 조회", description = "모든 room 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping
    public ResponseEntity<SuccessResponse<RoomInfoResponses>> getAllRooms() {
        var result = roomQueryService.findAllRoom();
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] roomID로 partition들 조회",
            description = "room id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<SuccessResponse<PartitionInfoResponses>> getPartitionsByRoomId(
            @ExistRoom @PathVariable Long roomId) {
        var result = roomQueryService.findPartitionsByRoomId(roomId);

        return ResponseFactory.success(result);
    }

}
