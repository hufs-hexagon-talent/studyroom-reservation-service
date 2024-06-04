package com.test.studyroomreservationsystem.apicontroller.admin;


import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.room.RoomDto;
import com.test.studyroomreservationsystem.dto.room.RoomUpdateDto;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Room", description = "방 정보 관련 API")
@RestController
@RequestMapping("/rooms")
public class AdminRoomController {
    private final RoomService roomService;
    @Autowired
    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "✅[관리자] room 생성",
            description = "room 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping("/room")
    public ResponseEntity<ApiResponseDto<RoomDto>> createRoom(@RequestBody RoomDto roomDto) {
        Room createdRoom = roomService.createRoom(roomDto);
        RoomDto room = roomService.dtoFrom(createdRoom);

        ApiResponseDto<RoomDto> response = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", room);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "✅[관리자] room 조회",
            description = "room id로 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId) {
        Room foundRoom = roomService.findRoomById(roomId);
        RoomDto room = roomService.dtoFrom(foundRoom);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @Operation(summary = "✅[관리자] 모든 room 조회",
            description = "모든 room 조회 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<RoomDto>>> getAllRooms() {
        List<RoomDto> rooms = roomService.findAllRoom()
                .stream()
                .map(roomService::dtoFrom)
                .collect(Collectors.toList());

        ApiResponseListDto<RoomDto> wrapped = new ApiResponseListDto<>(rooms);
        ApiResponseDto<ApiResponseListDto<RoomDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @Operation(summary = "❌[관리자] room 정보 수정",
            description = "해당 room id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long roomId,
                                              @RequestBody RoomUpdateDto roomDto) {
        Room updatedRoom = roomService.updateRoom(roomId, roomDto);
        RoomDto room = roomService.dtoFrom(updatedRoom);

        return new ResponseEntity<>(room, HttpStatus.OK);
    }
    @Operation(summary = "✅[관리자] room 삭제",
            description = "해당 room id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponseDto<Object> > deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        ApiResponseDto<Object> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
