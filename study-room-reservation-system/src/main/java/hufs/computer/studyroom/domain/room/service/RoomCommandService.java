package hufs.computer.studyroom.domain.room.service;

import hufs.computer.studyroom.common.service.CommonHelperService;
import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.room.dto.request.CreateRoomRequest;
import hufs.computer.studyroom.domain.room.dto.request.ModifyRoomRequest;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponse;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.mapper.RoomMapper;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomCommandService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final CommonHelperService commonHelperService;

    public RoomInfoResponse createRoom(CreateRoomRequest request) {
        Department department = commonHelperService.getDepartmentById(request.departmentId());
        Room room = roomMapper.toRoom(request, department);
        roomRepository.save(room);
        return roomMapper.toInfoResponse(room);
    }

    public RoomInfoResponse updateRoom(Long roomId, ModifyRoomRequest request) {
        Room room = commonHelperService.getRoomById(roomId);
        Department department = commonHelperService.getDepartmentById(request.departmentId());
        roomMapper.updateRoom(request, department, room);

        roomRepository.save(room);
        return roomMapper.toInfoResponse(room);
    }

    public void deleteRoom(Long roomId) {
        commonHelperService.getRoomById(roomId);
        roomRepository.deleteById(roomId);
    }
}
