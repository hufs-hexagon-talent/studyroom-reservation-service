package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.common.error.code.DepartmentErrorCode;
import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.redis.RedisService;
import hufs.computer.studyroom.domain.auth.service.JWTService;
import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.department.repository.DepartmentRepository;
import hufs.computer.studyroom.domain.department.service.DepartmentQueryService;
import hufs.computer.studyroom.domain.mail.dto.response.EmailResponse;
import hufs.computer.studyroom.domain.mail.service.MailService;
import hufs.computer.studyroom.domain.user.dto.request.*;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.mapper.UserMapper;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserQueryService userQueryService;
    private final DepartmentQueryService departmentQueryService;
    private final JWTService jwtService;
    private final MailService mailService;
    private final RedisService redisService;

    public UserInfoResponse signUpProcess(@Valid SignUpRequest request) {

        // 패스워드 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(request.password());
        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        User user = userMapper.toUser(request, encodedPassword, ServiceRole.USER, department);

        User savedUser = userRepository.save(user);

        return userMapper.toInfoResponse(savedUser);
    }

    @Transactional
    public UserInfoResponses signUpUsers(@Valid SignUpBulkRequest request) {
        List<SignUpRequest> signUpRequests = request.signUpRequests();
        // 1. 유효성 검증이 자동으로 처리되므로 별도의 검증 로직 필요 없음

        // 2. 모든 사용자의 유효성 검증이 통과된 후 회원가입 처리
        List<UserInfoResponse> responses = new ArrayList<>();

        // 모든 사용자의 유효성 검증이 통과된 후 회원가입 처리
        for (SignUpRequest signUpRequest : signUpRequests) {
            responses.add(signUpProcess(signUpRequest));
        }

        // 3. 저장한 사용자들 반환
        return userMapper.toInfoResponses(responses);
    }

    /**
     * 매 학기 재학생 데이터를 받아 사용자 상태를 업데이트하고 신규 사용자를 등록
     * - 기존 USER, BLOCKED → 재학생 데이터에 없으면 EXPIRED로 변경
     * - 기존 EXPIRED → 재학생 데이터에 있으면 USER로 변경
     * - 신규 학생 → 새로 등록 (초기 비밀번호는 학번)
     *
     * @param request 재학생 데이터 일괄 동기화 요청
     * @return 처리된 사용자 정보 목록
     */
    @Transactional
    public UserInfoResponses synchronizeEnroll(@Valid EnrollmentBulkRequest request) {
        List<EnrollmentRequest> enrollmentRequests = request.enrollments();
        List<UserInfoResponse> processedUsers = new ArrayList<>();

        // 1. 현재 재학생 데이터에서 학번(serial) 추출
        Map<String, EnrollmentRequest> currentEnrollmentMap = enrollmentRequests.stream()
                .collect(Collectors.toMap(
                        EnrollmentRequest::serial,
                        Function.identity(),
                        (existing, replacement) -> existing  // 중복 시 첫 번째 값 사용
                ));

        Set<String> currentEnrollmentSerials = currentEnrollmentMap.keySet();

        // 2. 시스템에 등록된 USER, EXPIRED 상태의 사용자 조회
        List<User> existingUsers
                = userQueryService.getUserByServiceRole(Arrays.asList(ServiceRole.USER, ServiceRole.EXPIRED, ServiceRole.BLOCKED));

        // 기존 사용자를 학번으로 매핑
        Map<String, User> existingUserMap = existingUsers.stream()
                .collect(Collectors.toMap(
                        User::getSerial,
                        Function.identity(),
                        (existing, replacement) -> existing  // 중복 시 첫 번째 값 사용
                ));

        // 3. 기존 사용자들의 상태 업데이트
        List<User> usersToUpdate = new ArrayList<>();

        for (User existingUser : existingUsers) {
            String userSerial = existingUser.getSerial();
            ServiceRole currentRole = existingUser.getServiceRole();

            if (currentEnrollmentSerials.contains(userSerial)) {
                // 재학생 데이터에 포함된 경우
                if (currentRole == ServiceRole.EXPIRED) {
                    // EXPIRED → USER로 변경
                    existingUser.setServiceRole(ServiceRole.USER);
                    usersToUpdate.add(existingUser);
                } else if (currentRole == ServiceRole.USER) {
                    // USER 상태인 경우는 유지
                    processedUsers.add(userMapper.toInfoResponse(existingUser));
                }
            } else {
                // 재학생 데이터에 포함되지 않은 경우
                if (currentRole == ServiceRole.USER || currentRole == ServiceRole.BLOCKED) {
                    // USER, BLOCKED → EXPIRED로 변경
                    existingUser.setServiceRole(ServiceRole.EXPIRED);
                    usersToUpdate.add(existingUser);
                }
                // EXPIRED 상태인 경우는 유지
            }
        }

        // 벌크 업데이트 수행
        if (!usersToUpdate.isEmpty()) {
            List<User> savedUsers = userRepository.saveAll(usersToUpdate);
            processedUsers.addAll(userMapper.toInfoResponseList(savedUsers));
        }

        // 4. 신규 사용자 등록 (시스템에 없는 학번)
        List<EnrollmentRequest> newUserRequests = currentEnrollmentMap.values().stream()
                .filter(req -> !existingUserMap.containsKey(req.serial()))
                .collect(Collectors.toList());


        int newUserCount = 0;
        int failedCount = 0;

        for (EnrollmentRequest enrollmentRequest : newUserRequests) {
            try {
                // EnrollmentRequest를 SignUpRequest로 변환 (초기 비밀번호는 학번)
                SignUpRequest signUpRequest = enrollmentRequest.toSignUpRequest();
                UserInfoResponse newUser = signUpProcess(signUpRequest);
                processedUsers.add(newUser);

                newUserCount++;
                log.info("신규 사용자 등록 성공: 학번 {}, 이름 {}, 학과ID {} (초기 비밀번호는 학번과 동일)",
                        enrollmentRequest.serial(),
                        enrollmentRequest.name(),
                        enrollmentRequest.departmentId());
            } catch (Exception e) {
                failedCount++;
                log.error("신규 사용자 등록 실패 - 학번: {}, 이름: {}, 오류: {}",
                        enrollmentRequest.serial(),
                        enrollmentRequest.name(),
                        e.getMessage());
                // 실패한 사용자는 건너뛰고 계속 진행
            }
        }

        // 5. 처리 결과 로깅
        log.info("===== 재학생 데이터 동기화 완료 =====");
        log.info("총 처리: {}명", processedUsers.size());
        log.info("신규 등록: {}명", newUserCount);
        log.info("상태 변경: {}명", usersToUpdate.size());
        log.info("실패: {}명", failedCount);
        log.info("=====================================");

        return userMapper.toInfoResponses(processedUsers);
    }

    public UserInfoResponse updateUserInfo(Long userId, ModifyUserInfoRequest request) {

        //1. 유저 조회
        User user = userQueryService.getUserById(userId);

        // 2. 소속 학과 조회 (departmentId가 null이 아닐 때만 변경 적용)
        Department department = null;
        if (request.departmentId() != null) {
            department = departmentQueryService.getDepartmentById(request.departmentId());
        }

        userMapper.updateUserFromRequest(request, user, department);
        User updatedUser = userRepository.save(user);
        return userMapper.toInfoResponse(updatedUser);
    }
    //  passwordToken 탈취 당하면 어떻게 하지???

    @Transactional
    public UserInfoResponse resetUserPasswordWithToken(ResetPasswordRequest request) {
        String email = jwtService.getEmailFromPasswordResetToken(request.token());

        Long userId = userQueryService.findByEmail(email).getUserId();
        return resetUserPassword(userId, request.newPassword());
    }

    public UserInfoResponse resetUserPasswordWithOldPassword(Long userId, ModifyPasswordRequest request) {
        User user = userQueryService.getUserById(userId);
        // 기존 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.prePassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_CURRENT_PASSWORD);
        }
        if (bCryptPasswordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_NEW_PASSWORD);
        }
        return resetUserPassword(userId, request.newPassword());
    }


    public EmailResponse authenticateForEmailModify(Long userId, ModifyEmailRequest request){
        User user = userQueryService.getUserById(userId);
        // 기존 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_CURRENT_PASSWORD);
        }
        // 신규 이메일 검증
        if (request.newEmail().equals(user.getEmail())){
            throw new CustomException(UserErrorCode.INVALID_NEW_EMAIL);
        }

        return mailService.sendAuthCodeToEmail(request.newEmail());
    }

    public UserInfoResponse authorizeEmailChange(Long userId, VerifyEmailRequest request){
        User user = userQueryService.getUserById(userId);

        String newEmail = mailService.verifyMailForMail(request);

        user.setEmail(newEmail);

        User savedUser = userRepository.save(user);
        return userMapper.toInfoResponse(savedUser);
    }


    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private UserInfoResponse resetUserPassword(Long userId, String newPassword) {
        User user = userQueryService.getUserById(userId);
        // 새 비밀번호 암호화 및 업데이트
        if (bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_NEW_PASSWORD);
        }
        String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        User savedUser = userRepository.save(user);
        return userMapper.toInfoResponse(savedUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void modifyServiceRoleById(Long userId, ServiceRole serviceRole){
        User user = userQueryService.getUserById(userId);
        user.setServiceRole(serviceRole);
        userRepository.save(user);
    }
}
