package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.code.ReservationErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.validation.annotation.ExistReservation;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.service.PartitionQueryService;
import hufs.computer.studyroom.domain.reservation.dto.request.CreateReservationRequest;
import hufs.computer.studyroom.domain.reservation.dto.request.ModifyReservationStateRequest;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponse;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.mapper.ReservationMapper;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.security.CustomUserDetails;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.entity.User.ServiceRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

import static hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState.NOT_VISITED;
import static hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState.VISITED;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final PartitionQueryService partitionQueryService;
    private final ReservationValidationService validateService;
    private final ReservationQueryService reservationQueryService;


    @Transactional
    public ReservationInfoResponse createReservation(CreateReservationRequest request, CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        Long roomPartitionId = request.roomPartitionId();

        // 검증
        validateService.validateRoomAvailability(user.getUserId(), roomPartitionId, request.startDateTime(), request.endDateTime());

        RoomPartition partition = partitionQueryService.getPartitionById(roomPartitionId);

        // default state : NOT_VISITED
        Reservation reservation = reservationMapper.toReservation(request, user, partition, NOT_VISITED);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toInfoResponse(savedReservation);
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


}
