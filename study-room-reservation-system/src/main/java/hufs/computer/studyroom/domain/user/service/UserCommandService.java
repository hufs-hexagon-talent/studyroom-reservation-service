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

import java.util.ArrayList;
import java.util.List;

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
