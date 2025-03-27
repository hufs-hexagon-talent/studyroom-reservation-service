package hufs.computer.studyroom.domain.reservation.mapper;

import hufs.computer.studyroom.domain.checkin.dto.response.CheckInResponse;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.reservation.dto.request.CreateReservationRequest;
import hufs.computer.studyroom.domain.reservation.dto.request.ModifyReservationStateRequest;
import hufs.computer.studyroom.domain.reservation.dto.response.*;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    // CreateReservationRequest -> Reservation 엔티티 변환
    @Mapping(source = "user", target = "user")
    @Mapping(source = "partition", target = "roomPartition")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "request.startDateTime", target = "reservationStartTime")
    @Mapping(source = "request.endDateTime", target = "reservationEndTime")
    @Mapping(target = "createAt", ignore = true) // createAt은 엔티티 생성 시 자동으로 설정됨
    @Mapping(target = "updateAt", ignore = true) // updateAt은 수정 시 자동으로 설정됨
    @Mapping(target = "reservationId", ignore = true) // 예약 ID는 자동 생성됨
    Reservation toReservation(CreateReservationRequest request, User user, RoomPartition partition, ReservationState state);

    // Reservation -> ReservationInfoResponse DTO 변환
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "roomPartition.room.roomId", target = "roomId")
    @Mapping(source = "roomPartition.room.roomName", target = "roomName")
    @Mapping(source = "roomPartition.room.department.departmentId", target = "departmentId")
    @Mapping(source = "roomPartition.room.department.departmentName", target = "departmentName")
    @Mapping(source = "roomPartition.roomPartitionId", target = "roomPartitionId")
    @Mapping(source = "roomPartition.partitionNumber", target = "partitionNumber")
    @Mapping(source = "state", target = "reservationState")
    ReservationInfoResponse toInfoResponse(Reservation reservation);

    // ModifyReservationInfoRequest -> 기존 Reservation 엔티티의 state 수정
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "roomPartition", ignore = true)
    @Mapping(target = "reservationStartTime", ignore = true)
    @Mapping(target = "reservationEndTime", ignore = true)
    @Mapping(target = "createAt", ignore = true) // createAt은 엔티티 생성 시 자동으로 설정됨
    @Mapping(target = "updateAt", ignore = true) // updateAt은 수정 시 자동으로 설정됨
    @Mapping(target = "reservationId", ignore = true) // 예약 ID는 자동 생성됨
    void updateStateFromRequest(ModifyReservationStateRequest request, @MappingTarget Reservation reservation);

    // Reservation -> TimeRange 변환
    @Mapping(source = "reservationId", target = "reservationId")
    @Mapping(source = "reservationStartTime", target = "startDateTime")
    @Mapping(source = "reservationEndTime", target = "endDateTime")
    ReservationTimeRange toTimeRange(Reservation reservation);

    // RoomPartition, RoomOperationPolicy, List<ReservationTimeRange> -> PartitionReservationStatus 변환
    @Mapping(source = "partition.room.roomId", target = "roomId")
    @Mapping(source = "partition.room.roomName", target = "roomName")
    @Mapping(source = "partition.roomPartitionId", target = "partitionId")
    @Mapping(source = "partition.partitionNumber", target = "partitionNumber")
    @Mapping(source = "policy.roomOperationPolicyId", target = "roomOperationPolicyId")
    @Mapping(source = "policy.operationStartTime", target = "operationStartTime")
    @Mapping(source = "policy.operationEndTime", target = "operationEndTime")
    @Mapping(source = "policy.eachMaxMinute", target = "eachMaxMinute")
    PartitionReservationStatus toPartitionReservationStatus(RoomPartition partition, RoomOperationPolicy policy, List<ReservationTimeRange> reservationTimeRanges);


    @Mapping(target = "totalReservations", source = "totalReservations")
    @Mapping(target = "todayReservations", source = "todayReservations")
    @Mapping(target = "weeklyReservations", source = "weeklyReservations")
    @Mapping(target = "monthlyReservations", source = "monthlyReservations")
    ReservationStaticResponse toReservationStatic(Long totalReservations,
                                                  Long todayReservations,
                                                  Long weeklyReservations,
                                                  Long monthlyReservations);


    // List<PartitionReservationStatus> -> AllPartitionsReservationStatusResponse 변환
    default AllPartitionsReservationStatusResponse toAllPartitionsReservationStatusResponse(List<PartitionReservationStatus> partitionReservationStatuses) {
        return AllPartitionsReservationStatusResponse.builder()
                .partitionReservationInfos(partitionReservationStatuses)
                .build();
    }

    // noShowCount와 ReservationInfoResponses -> UserNoShowCntResponse 변환
    default UserNoShowCntResponse toUserNoShowCntResponse(int noShowCount, List<Reservation> reservations) {
        ReservationInfoResponses reservationInfoResponses = toInfoResponses(reservations);
        return new UserNoShowCntResponse(noShowCount, reservationInfoResponses);
    }
    // todo : 테스트 하기 ~
    default BlockedUserNoShowResponses toUserNoShowCntResponses(List<UserNoShowCntResponse> userNoShowCntResponses) {
        return new BlockedUserNoShowResponses(userNoShowCntResponses);
    }

    // List<Reservation> -> ReservationInfoResponses 변환
    default ReservationInfoResponses toInfoResponses(List<Reservation> reservations) {
        List<ReservationInfoResponse> reservationInfoResponses = reservations.stream()
                .map(this::toInfoResponse)
                .toList();
        return new ReservationInfoResponses(reservationInfoResponses);
    }
    // List<Reservation>와 CheckIn 시간 -> CheckInResponse 생성
    default CheckInResponse toCheckInResponse(List<Reservation> reservations, Instant checkInTime) {
        ReservationInfoResponses reservationInfoResponses = toInfoResponses(reservations);
        return new CheckInResponse(reservationInfoResponses, checkInTime);
    }
}
