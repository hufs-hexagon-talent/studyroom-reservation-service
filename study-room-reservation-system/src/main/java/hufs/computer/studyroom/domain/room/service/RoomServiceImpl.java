package hufs.computer.studyroom.domain.room.service;

import hufs.computer.studyroom.common.error.exception.reservation.RoomPolicyNotFoundException;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.room.dto.RoomDto;
import hufs.computer.studyroom.domain.room.dto.RoomUpdateRequestDto;
import hufs.computer.studyroom.domain.room.dto.RoomResponseDto;
import hufs.computer.studyroom.common.error.exception.reservation.OperationClosedException;
import hufs.computer.studyroom.common.util.DateTimeUtil;
import hufs.computer.studyroom.common.error.exception.notfound.RoomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomOperationPolicyScheduleRepository scheduleRepository;

    public RoomServiceImpl(RoomRepository roomRepository,
                           RoomOperationPolicyScheduleRepository scheduleRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Room createRoom(RoomDto roomDto) {
        Room roomEntity = roomDto.toEntity();
        return roomRepository.save(roomEntity);
    }

    @Override
    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
    }

    @Override
    public Room findRoomByName(String roomName) {
        return roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RoomNotFoundException(roomName));
    }

    @Override
    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Room updateRoom(Long roomId, RoomUpdateRequestDto roomUpdateDto) {
        Room roomEntity = findRoomById(roomId);
        roomEntity.setRoomName(roomUpdateDto.getRoomName());

        return roomRepository.save(roomEntity);
    }

    @Override
    public void deleteRoom(Long roomId) {
        findRoomById(roomId); // 찾아보고 없으면 예외처리
        roomRepository.deleteById(roomId);
    }

    @Override // 룸이 운영을 하는지? && 운영이 종료 되었는지?
    public void isRoomAvailable(Long roomId, Instant reservationStartTime, Instant reservationEndTime) {

        Room room = findRoomById(roomId);
        LocalDate date = reservationStartTime.atZone(ZoneOffset.UTC).toLocalDate();


        // 룸과 날짜로 정책 찾기
        RoomOperationPolicySchedule schedule
                = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                .orElseThrow(
                        // 운영이 하지 않음 (운영 정책 없음)
                        () -> new RoomPolicyNotFoundException(room, date)
                );


        RoomOperationPolicy roomOperationPolicy = schedule.getRoomOperationPolicy();


        Instant operationStartTime
                = DateTimeUtil.convertKstToUtc(
                        date.atTime(roomOperationPolicy.getOperationStartTime())
        ).atZone(ZoneOffset.UTC).toInstant();

        Instant operationEndTime
                = DateTimeUtil.convertKstToUtc(
                        date.atTime(roomOperationPolicy.getOperationEndTime())
        ).atZone(ZoneOffset.UTC).toInstant();

        boolean operationBefore = reservationStartTime.isBefore(operationStartTime);
        log.info("reservationstartTime < operationStartTime 인지 {}", operationBefore);

        boolean operationAfter = reservationEndTime.isAfter(operationEndTime);
        log.info("operationEndTime < reservationEndTime 인지 {}", operationAfter);

        if (operationAfter || operationBefore) {
            throw new OperationClosedException(
                    room,
                    operationStartTime, operationEndTime,
                    reservationStartTime, reservationEndTime);
        }

    }

    @Override
    public List<RoomResponseDto> getRoomsPolicyByDate(LocalDate date) {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponseDto> responseList = new ArrayList<>();

        for (Room room : rooms) {
            RoomOperationPolicy policy = null;
            try {
                // 룸과 날짜로 정책 찾기
                RoomOperationPolicySchedule schedule
                        = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                        .orElseThrow(
                                // 운영이 하지 않음 (운영 정책 없음)
                                () -> new RoomPolicyNotFoundException(room, date)
                        );
                policy = schedule.getRoomOperationPolicy();

            } catch (RoomPolicyNotFoundException e) {
                // 정책이 없을 때는 policyId가 null로 유지됨
            }
            responseList.add(new RoomResponseDto(room.getRoomId(), room.getRoomName(), policy));
        }
        return responseList;
    }

}