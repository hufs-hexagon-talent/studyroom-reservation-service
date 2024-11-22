package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.user.dto.response.UserBlockedInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserBlockedInfoResponses;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.mapper.UserMapper;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {

    @Value("${spring.service.noShowLimit}") private int noShowLimit;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReservationQueryService reservationQueryService;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS));
    }

    public UserInfoResponses findAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toInfoResponses(userMapper.toInfoResponseList(users));
    }

    public UserInfoResponse findUserById(Long userId) {
        User user = getUserById(userId);
        return userMapper.toInfoResponse(user);
    }

    public UserInfoResponse findUserBySerial(String serial) {
        User user = userRepository.findBySerial(serial).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return userMapper.toInfoResponse(user);
    }

    public UserInfoResponses findUserByName(String name) {
        List<User> users = userRepository.findByName(name);
        return userMapper.toInfoResponses(userMapper.toInfoResponseList(users));
    }

    public UserBlockedInfoResponses findBlockedUser() {
        List<User> blockedUsers = getBlockedUsers();

        List<UserBlockedInfoResponse> blockedInfoResponses = blockedUsers.stream()
                .map(user -> userMapper.toBlockedInfoResponse(
                        user,
                        reservationQueryService.getBlockedStartTime(user.getUserId()),
                        reservationQueryService.getBlockedEndTime(user.getUserId())
                ))
                .collect(Collectors.toList());

        return userMapper.toBlockedInfoResponses(blockedInfoResponses);
    }
    public UserBlockedInfoResponse getUserBlockedPeriod(CustomUserDetails currentUser){
        User user = currentUser.getUser(); // todo : 검증 계층으로 빼기
        if (!user.getServiceRole().equals(ServiceRole.BLOCKED)) {
            throw new CustomException(UserErrorCode.INVALID_SERVICE_ROLE);
        }
        return userMapper.toBlockedInfoResponse(user,
                reservationQueryService.getBlockedStartTime(user.getUserId()),
                reservationQueryService.getBlockedEndTime(user.getUserId()));
    }

    /**
     * 사용자가 No-Show 제한에 걸렸는지 확인하는 메서드
     */
    public boolean isUserBlockedDueToNoShow(Long userId) {
        List<Reservation> noShowReservations = reservationQueryService.getNoShowReservationsByUserId(userId);
        return noShowReservations.size() >= noShowLimit;
    }

    public List<User> getBlockedUsers() {
        return userRepository.getBlockedUsers();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    public ServiceRole getServiceRoleById(Long id) {return userRepository.findUserServiceRoleByUserId(id);}
    public boolean existByUserId(Long userId) {return userRepository.existsById(userId);}
    public boolean existByUsername(String username) {return userRepository.existsByUsername(username);}
    public boolean existBySerial(String serial) {return userRepository.existsBySerial(serial);}
    public boolean existByEmail(String email) {return userRepository.existsByEmail(email);}

    public boolean isServiceRoleUSER(Long userId) {return getServiceRoleById(userId) == ServiceRole.USER;}
    public boolean isServiceRoleBLOCKED(Long userId) {return getServiceRoleById(userId) == ServiceRole.BLOCKED;}
}
