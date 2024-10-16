package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.common.error.code.DepartmentErrorCode;
import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.service.CommonHelperService;
import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.department.repository.DepartmentRepository;
import hufs.computer.studyroom.domain.user.dto.request.*;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.mapper.UserMapper;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import hufs.computer.studyroom.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
    private final CommonHelperService commonHelperService;
    private final UserQueryService userQueryService;

    public UserInfoResponse signUpProcess(SignUpRequest request) {
        validateUser(request);
        String encodedPassword = bCryptPasswordEncoder.encode(request.password());
        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        User user = userMapper.toUser(request, encodedPassword, User.ServiceRole.USER, department);

        User savedUser = userRepository.save(user);

        return userMapper.toInfoResponse(savedUser);
    }

    @Transactional
    public UserInfoResponses signUpUsers(SignUpBulkRequest request) {
        List<SignUpRequest> signUpRequests = request.signUpRequests();

        // 1. 유효성 검증 실패 시 롤백 처리
        for (SignUpRequest signUpRequest : signUpRequests) {
            validateUser(signUpRequest);  // 각 사용자의 유효성 먼저 검증
        }

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
        User user = commonHelperService.getUserById(userId); // todo : 추후 validator 리팩토링
        Department department = commonHelperService.getDepartmentById(request.departmentId());
        userMapper.updateUserFromRequest(request, user, department);
        User updatedUser = userRepository.save(user);
        return userMapper.toInfoResponse(updatedUser);
    }

    public UserInfoResponse resetUserPasswordWithToken(ResetPasswordRequest request) {
        String email = jwtUtil.getEmail(request.token());
        Long userId = userQueryService.findByEmail(email).getUserId();
        return resetUserPassword(userId, request.newPassword());
    }

    public UserInfoResponse resetUserPasswordWithOldPassword(Long userId, ModifyPasswordRequest request) {
        User user = commonHelperService.getUserById(userId); // todo : 추후 validator 리팩토링
        // 기존 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.prePassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_CURRENT_PASSWORD);
        }
        return resetUserPassword(userId, request.newPassword());
    }

    public void deleteUser(Long userId) {
        commonHelperService.getUserById(userId); // todo : 추후 validator 리팩토링
        userRepository.deleteById(userId);
    }


    private void validateUser(SignUpRequest request) {
//        todo validator 쪽으로 옮겨야해
        // 해당 로그인 ID 가 이미 존재하는지 확인
        if (userRepository.existsByUsername(request.username())) {
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
        // 학번 중복 확인
        if (userRepository.existsBySerial(request.serial())) {
            throw new CustomException(UserErrorCode.SERIAL_ALREADY_EXISTS);
        }
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private UserInfoResponse resetUserPassword(Long userId, String newPassword) {
        User user = commonHelperService.getUserById(userId); // todo : 추후 validator 리팩토링
        // 새 비밀번호 암호화 및 업데이트
        if (bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_NEW_PASSWORD);
        }
        String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        User savedUser = userRepository.save(user);
        return userMapper.toInfoResponse(savedUser);
    }
}
