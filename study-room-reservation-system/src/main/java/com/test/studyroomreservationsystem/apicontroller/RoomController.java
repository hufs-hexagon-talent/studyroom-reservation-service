package com.test.studyroomreservationsystem.apicontroller;


import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.dto.RoomDto;
import com.test.studyroomreservationsystem.dto.RoomUpdateDto;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Room", description = "방 정보 관련 API")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "room 생성", description = "room 생성하는 API")
    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        Room room = roomService.createRoom(roomDto);
        RoomDto createdRoom = roomService.convertToDto(room);

        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }
    @Operation(summary = "room 조회", description = "room id로 조회 API")
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId) {
        Room room = roomService.findRoomById(roomId);
        RoomDto foundRoom = roomService.convertToDto(room);
        return new ResponseEntity<>(foundRoom, HttpStatus.OK);
    }

    @Operation(summary = "모든 room 조회", description = "모든 room 조회 API")
    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<Room> rooms = roomService.findAllRoom();
        List<RoomDto> roomDtoList = rooms.stream()
                .map(roomService::convertToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(roomDtoList, HttpStatus.OK);

    }
    @Operation(summary = "room 정보 업데이트", description = "해당 room id의 정보 업데이트 API")
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long roomId,
                                              @RequestBody RoomUpdateDto roomDto) {
        Room room = roomService.updateRoom(roomId, roomDto);
        RoomDto updatedRoom = roomService.convertToDto(room);

        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }
    @Operation(summary = "room 삭제", description = "해당 room id의 정보 삭제 API")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
