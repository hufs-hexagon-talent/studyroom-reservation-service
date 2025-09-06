package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.response.PageMeta;
import hufs.computer.studyroom.common.response.PageResponse;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.user.dto.excel.UserExportExcelDto;
import hufs.computer.studyroom.domain.user.dto.request.UserSearchCondition;
import hufs.computer.studyroom.domain.user.dto.response.*;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.mapper.UserMapper;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import hufs.computer.studyroom.domain.user.repository.UserSpecification;
import hufs.computer.studyroom.domain.user.repository.projection.ServiceRoleStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {

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

    public PageResponse<UserInfoResponse> searchUsers(UserSearchCondition condition) {
        Pageable pageable = PageRequest.of(
                Optional.ofNullable(condition.page()).orElse(0),
                Optional.ofNullable(condition.size()).orElse(30),
                Sort.by(Sort.Direction.ASC, "userId")   // 기본 정렬
        );

        Page<User> pageResult =
                userRepository.findAll(UserSpecification.search(condition), pageable);

        List<UserInfoResponse> items =
                userMapper.toInfoResponseList(pageResult.getContent());

        PageMeta meta = new PageMeta(
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
        return new PageResponse<>(meta, items);
    }

    /**
     * 블락된 유저들 찾아서 블락 기간 및 유저 정보 찾기
     */
    public UserBlockedInfoResponses findBlockedUser() {
        List<User> blockedUsers = getBlockedUsers();

        List<UserBlockedInfoResponse> blockedInfoResponses = blockedUsers.stream()
                .map(this::createUserBlockedInfoResponse)
                .collect(Collectors.toList());

        return userMapper.toBlockedInfoResponses(blockedInfoResponses);
    }


    public UserBlockedInfoResponse getUserBlockedPeriod(CustomUserDetails currentUser){
        User user = currentUser.getUser();
        validateUserIsBlocked(user);
        return createUserBlockedInfoResponse(user);
    }

    public UserStaticResponse getUserStatics(){
        ServiceRoleStats countUserByServiceRole = userRepository.getCountUserByServiceRole();

        return userMapper.toUserStaticResponse(countUserByServiceRole);
    }

    /*
    * 검증 로직: 유저가 BLOCKED 상태인지 확인
    * */
    private void validateUserIsBlocked(User user) {
        if (!user.getServiceRole().equals(ServiceRole.BLOCKED)) {
            throw new CustomException(UserErrorCode.INVALID_SERVICE_ROLE);
        }
    }
    /**
     * User 객체로부터 UserBlockedInfoResponse 생성
     */
    private UserBlockedInfoResponse createUserBlockedInfoResponse(User user) {
        LocalDate blockedStartTime = reservationQueryService.getBlockedStartTime(user.getUserId());
        LocalDate blockedEndTime = reservationQueryService.getBlockedEndTime(user.getUserId());
        return userMapper.toBlockedInfoResponse(user, blockedStartTime, blockedEndTime);
    }

    public List<User> getBlockedUsers() {
        return userRepository.getBlockedUsers();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    public ServiceRole getServiceRoleById(Long id) {return userRepository.findUserServiceRoleByUserId(id);}

    public List<User> getUserByServiceRole(Collection<ServiceRole> filterByRoles) {
        return userRepository.findUsersByServiceRole(filterByRoles);
    }
    /*
     * 엑셀 다운로드 용 DTO 리스트 반환
     */
    public List<UserExportExcelDto> getExcelDTOs(Collection<ServiceRole> filterByRoles){

        // (필터 조건) USER 와 BLOCKED 만
        List<User> users = getUserByServiceRole(filterByRoles);
        return userMapper.toExportExcelDTOs(users);
    }

    public boolean existByUserId(Long userId) {return userRepository.existsById(userId);}
    public boolean existByUsername(String username) {return userRepository.existsByUsername(username);}
    public boolean existBySerial(String serial) {return userRepository.existsBySerial(serial);}
    public boolean existByEmail(String email) {return userRepository.existsByEmail(email);}

}
