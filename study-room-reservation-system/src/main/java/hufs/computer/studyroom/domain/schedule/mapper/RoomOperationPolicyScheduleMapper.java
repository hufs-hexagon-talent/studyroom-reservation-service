package hufs.computer.studyroom.domain.schedule.mapper;

import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.dto.request.ModifyScheduleRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.AvailableDateResponses;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleTableResponse;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoomOperationPolicyScheduleMapper {

    // CreateScheduleBulkRequest의 단일 Schedule -> RoomOperationPolicySchedule 변환
    @Mapping(target = "roomOperationPolicyScheduleId", ignore = true)
    @Mapping(source = "policy", target = "roomOperationPolicy")
    RoomOperationPolicySchedule toRoomOperationPolicySchedule(RoomOperationPolicy policy, Room room, LocalDate policyApplicationDate);

    // 단일 RoomOperationPolicySchedule -> ScheduleInfoResponse 변환
    @Mapping(source = "room.roomId", target = "roomId")
    @Mapping(source = "roomOperationPolicy.roomOperationPolicyId", target = "roomOperationPolicyId")
    ScheduleInfoResponse toScheduleInfoResponse(RoomOperationPolicySchedule schedule);

    // ModifyScheduleRequest -> 기존 Schedule 엔티티 수정
    @Mapping(target = "roomOperationPolicyScheduleId", ignore = true)
    void updateFromRequest(ModifyScheduleRequest request,Room room,RoomOperationPolicy roomOperationPolicy, @MappingTarget RoomOperationPolicySchedule schedule);

    @Mapping(source = "roomOperationPolicyScheduleId", target = "scheduleId")
    @Mapping(source = "room.roomId", target = "roomId")
    @Mapping(source = "room.roomName", target = "roomName")
    @Mapping(source = "roomOperationPolicy.roomOperationPolicyId", target = "policyId")
    @Mapping(source = "roomOperationPolicy.operationStartTime", target = "operationStartTime")
    @Mapping(source = "roomOperationPolicy.operationEndTime", target = "operationEndTime")
    @Mapping(source = "roomOperationPolicy.eachMaxMinute", target = "eachMaxMinute")
    @Mapping(source = "policyApplicationDate", target = "policyApplicationDate")
    ScheduleTableResponse toScheduleOneLineResponse(RoomOperationPolicySchedule schedule);

    // 리스트 변환 (RoomOperationPolicySchedule 리스트 -> ScheduleInfoResponse 리스트)
    List<ScheduleInfoResponse> toScheduleInfoResponseList(List<RoomOperationPolicySchedule> schedules);

    default List<ScheduleTableResponse> toScheduleTableResponse(List<RoomOperationPolicySchedule> schedules){
        return schedules.stream()
                .map(this::toScheduleOneLineResponse)
                .collect(Collectors.toList());

    }

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
