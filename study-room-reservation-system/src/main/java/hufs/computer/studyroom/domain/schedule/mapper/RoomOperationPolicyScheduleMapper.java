package hufs.computer.studyroom.domain.schedule.mapper;

import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.dto.request.CreateScheduleBulkRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.AvailableDateResponses;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomOperationPolicyScheduleMapper {
    // CreateScheduleBulkRequest의 단일 Schedule -> RoomOperationPolicySchedule 변환
    RoomOperationPolicySchedule toRoomOperationPolicySchedule(RoomOperationPolicy policy, Room room, LocalDate policyApplicationDate);

    // 단일 RoomOperationPolicySchedule -> ScheduleInfoResponse 변환
    @Mapping(source = "room.roomId", target = "roomId")
    @Mapping(source = "roomOperationPolicy.roomOperationPolicyId", target = "roomOperationPolicyId")
    ScheduleInfoResponse toScheduleInfoResponse(RoomOperationPolicySchedule schedule);

    // 리스트 변환 (RoomOperationPolicySchedule 리스트 -> ScheduleInfoResponse 리스트)
    List<ScheduleInfoResponse> toScheduleInfoResponseList(List<RoomOperationPolicySchedule> schedules);

    // 여러 RoomOperationPolicySchedule 리스트를 감싸는 응답 생성
    default ScheduleInfoResponses toScheduleInfoResponses(List<RoomOperationPolicySchedule> schedules) {
        return ScheduleInfoResponses.builder()
                .schedules(toScheduleInfoResponseList(schedules))
                .build();
    }

    default AvailableDateResponses toAvailableDateResponses(List<LocalDate> dates) {
        return AvailableDateResponses.builder()
                .availableDates(dates)
                .build();
    }
}
