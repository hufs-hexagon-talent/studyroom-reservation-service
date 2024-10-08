package hufs.computer.studyroom.domain.room.service;

import hufs.computer.studyroom.common.error.code.DepartmentErrorCode;
import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.department.repository.DepartmentRepository;
import hufs.computer.studyroom.domain.partition.dto.response.PartitionInfoResponses;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.mapper.RoomPartitionMapper;
import hufs.computer.studyroom.domain.partition.repository.RoomPartitionRepository;
import hufs.computer.studyroom.domain.policy.service.RoomOperationPolicyService;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponses;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.mapper.RoomMapper;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import hufs.computer.studyroom.domain.room.dto.request.CreateRoomRequest;
import hufs.computer.studyroom.domain.room.dto.request.ModifyRoomRequest;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomPartitionRepository partitionRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomPartitionMapper partitionMapper;
    private final RoomOperationPolicyService policyService;
    private final RoomMapper roomMapper;


    @Override
    public RoomInfoResponse createRoom(CreateRoomRequest request) {
        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
        Room room = roomMapper.toRoom(request, department);
        roomRepository.save(room);
        return roomMapper.toInfoResponse(room);
    }

    @Override
    public RoomInfoResponse findRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        return roomMapper.toInfoResponse(room);
    }

    @Override
    public RoomInfoResponses findAllRoom() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.toRoomInfoResponses(rooms);
    }

    @Override
    public RoomInfoResponse updateRoom(Long roomId, ModifyRoomRequest request) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        if (request.roomName() != null) {
            room.setRoomName(request.roomName());
        }
        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
            room.setDepartment(department);
        }

        roomRepository.save(room);
        return roomMapper.toInfoResponse(room);
    }

    @Override
    public PartitionInfoResponses findPartitionsByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        List<RoomPartition> partitions = partitionRepository.findByRoom_RoomId(roomId);
        return partitionMapper.toPartitionInfoResponses(partitions);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.findById(roomId).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        roomRepository.deleteById(roomId);
    }

    @Override // 룸이 운영을 하는지? && 운영이 종료 되었는지?
    public void isRoomAvailable(Long roomId, Instant reservationStartTime, Instant reservationEndTime) {

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
        policyService.validateRoomOperation(room,reservationStartTime,reservationEndTime);
    }

}
