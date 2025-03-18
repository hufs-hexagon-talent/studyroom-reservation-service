package hufs.computer.studyroom.domain.checkin.service;

import hufs.computer.studyroom.common.error.code.CheckInErrorCode;
import hufs.computer.studyroom.common.error.code.RedisErrorCode;
import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.redis.RedisService;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.room.service.RoomQueryService;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckInValidationService {
    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;
    private final RedisService redisService;
    /**
     * CheckIn 요청에 대해서 방에 파티션이 있는 검증하기 + 룸에 대한 파티션 정보 반환
     */
    public List<Long> validateRoomId(Long roomId) {
        List<Long> partitionIds = roomQueryService.getPartitionsByRoomId(roomId).stream().map(RoomPartition::getRoomPartitionId).toList();
        if (partitionIds.isEmpty()) {throw new CustomException(RoomErrorCode.ROOM_HAS_NOT_PARTITION);}
        return partitionIds;
    }

    /**
     * CheckIn 요청에 대해서 OTP 코드가 유효한지 검증하기 + user 정보 반환
     */
    public Long validateVerificationCode(String verificationCode) {
        Long userId;
        try {
            userId = Long.valueOf(redisService.getValue(verificationCode));
        } catch (CustomException exception) {
            if (exception.getErrorCode() == RedisErrorCode.REDIS_KEY_NOT_FOUND) {
                throw new CustomException(CheckInErrorCode.OTP_NOT_FOUND);
            } else {
                throw exception;
            }
        }
        userQueryService.getUserById(userId);
        return userId;
    }

// -------------------------------------------------------------------------------------------------------------------------
}
