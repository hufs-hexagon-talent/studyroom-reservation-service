package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
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

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    public boolean existByUserId(Long userId) {return userRepository.existsById(userId);}
    public boolean existByUsername(String username) {return userRepository.existsByUsername(username);}
    public boolean existBySerial(String serial) {return userRepository.existsBySerial(serial);}
    public boolean existByEmail(String email) {return userRepository.existsByEmail(email);}
}