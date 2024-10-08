package hufs.computer.studyroom.domain.room.mapper;

import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.room.dto.request.CreateRoomRequest;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponse;
import hufs.computer.studyroom.domain.room.dto.response.RoomInfoResponses;
import hufs.computer.studyroom.domain.room.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    // CreateRoomRequest -> Room 엔티티 변환
    @Mapping(target = "department", source = "department")
    Room toRoom(CreateRoomRequest request, Department department);

    // Room -> RoomInfoResponse 변환
    @Mapping(source = "room.department.departmentId", target = "departmentId")
    @Mapping(source = "room.department.departmentName", target = "departmentName")
    RoomInfoResponse toInfoResponse(Room room);

    // Room 업데이트 (ModifyRoomRequest가 있다고 가정)
    @Mapping(target = "department", source = "department")
    Room updateRoom(CreateRoomRequest request, Department department, @MappingTarget Room room);

    default RoomInfoResponses toRoomInfoResponses(List<Room> rooms) {
        List<RoomInfoResponse> responses = rooms.stream()
                .map(this::toInfoResponse)
                .toList();
        return RoomInfoResponses.builder()
                .rooms(responses)
                .build();
    }
}
