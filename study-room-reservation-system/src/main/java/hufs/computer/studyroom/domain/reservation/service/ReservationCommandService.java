package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.code.ReservationErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.service.PartitionQueryService;
import hufs.computer.studyroom.domain.reservation.dto.request.CreateReservationRequest;
import hufs.computer.studyroom.domain.reservation.dto.request.ModifyReservationStateRequest;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponse;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.mapper.ReservationMapper;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.entity.User;

import hufs.computer.studyroom.domain.user.service.UserCommandService;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import hufs.computer.studyroom.domain.user.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

import static hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState.NOT_VISITED;
import static hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState.VISITED;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final PartitionQueryService partitionQueryService;
    private final ReservationValidationService validationService;
    private final ReservationQueryService reservationQueryService;
    private final UserCommandService userCommandService;
    private final UserValidationService userValidationService;
    private final UserQueryService userQueryService;


    @Transactional
    public ReservationInfoResponse createReservation(CreateReservationRequest request, CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        Long roomPartitionId = request.roomPartitionId();

        // No Show 및 예약 가능 여부 검증
        validateReservationRequest(user, roomPartitionId, request.startDateTime(), request.endDateTime());

        RoomPartition partition = partitionQueryService.getPartitionById(roomPartitionId);

        // default state : NOT_VISITED
        Reservation reservation = reservationMapper.toReservation(request, user, partition, NOT_VISITED);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toInfoResponse(savedReservation);
    }

    // No Show 및 예약 가능 여부 검증
    private void validateReservationRequest(User user, Long roomPartitionId, Instant startDateTime, Instant endDateTime) {
        Long userId = user.getUserId();
        ServiceRole userRole = userQueryService.getServiceRoleById(userId);

        // [검증] 0. ADMIN, RESIDENT 인 경우 [검증]1. 하지 않음
        if (userRole == ServiceRole.USER || userRole == ServiceRole.BLOCKED) {
            // [검증] 1. No Show 횟수에 따른 차단 여부 확인
            if (userValidationService.isBlocked(userId)) {
                throw new CustomException(ReservationErrorCode.NO_SHOW_LIMIT_EXCEEDED);
            }
        }

        // [검증] 2. 예약 가능 여부 검증
        validationService.validateRoomAvailability(userId, roomPartitionId, startDateTime, endDateTime);
    }

    public void deleteReservationBySelf(Long reservationId, CustomUserDetails currentUser) {

        User user = currentUser.getUser();
        Reservation reservation = reservationQueryService.getReservationById(reservationId);

        // 예약이 본인의 것인지 확인
        if (!reservation.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }

        // 이미 방문 처리된 예약인지 확인
        if (reservation.getState() == VISITED) {
            throw new CustomException(ReservationErrorCode.RESERVATION_ALREADY_VISITED);
        }

        if (reservation.getReservationStartTime().isBefore(Instant.now())) {
            throw new CustomException(ReservationErrorCode.RESERVATION_ALREADY_STARTED);
        }
        reservationRepository.deleteById(reservationId);
    }

// todo
    public void deleteReservationByAdmin(Long reservationId, CustomUserDetails currentUser) {
        User user = currentUser.getUser();
//        // 관리자 권한 확인
//        if (user.getServiceRole() != ServiceRole.ADMIN) {
//            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
//        }
        reservationRepository.deleteById(reservationId);
    }

    public ReservationInfoResponse updateReservationState(Long reservationId, ModifyReservationStateRequest request) {
        Reservation reservation = reservationQueryService.getReservationById(reservationId);

        reservationMapper.updateStateFromRequest(request, reservation);
        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.toInfoResponse(saved);
    }

    /**
     *  No-Show 상태의 예약들을 PROCESSED 상태로 업데이트 & ServiceRole BLOCKED -> USER
     */
    public ReservationInfoResponses updateNoShowReservationsToProcessed(Long userId) {
        ServiceRole currentRole = userQueryService.getServiceRoleById(userId);
        if (currentRole == ServiceRole.ADMIN || currentRole == ServiceRole.RESIDENT){
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }

        List<Reservation> noShowReservations = reservationQueryService.getNoShowReservationsByUserId(userId);
        noShowReservations.forEach(reservation -> reservation.setState(Reservation.ReservationState.PROCESSED));

        reservationRepository.saveAll(noShowReservations);

        if (currentRole == ServiceRole.BLOCKED) {
            userCommandService.modifyServiceRoleById(userId, ServiceRole.USER);
        }

        return reservationMapper.toInfoResponses(reservationRepository.saveAll(noShowReservations));
    }

}
