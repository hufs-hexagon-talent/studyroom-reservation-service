package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.service.CommonHelperService;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.mapper.UserMapper;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CommonHelperService commonHelperService;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return userMapper.toInfoResponse(user);
    }

}
