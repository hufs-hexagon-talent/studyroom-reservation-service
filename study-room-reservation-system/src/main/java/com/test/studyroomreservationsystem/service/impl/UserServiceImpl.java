package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import com.test.studyroomreservationsystem.dto.user.UserInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.dto.user.editpassword.UserPasswordInfoResetRequestDto;
import com.test.studyroomreservationsystem.dto.user.SingUpRequestDto;
import com.test.studyroomreservationsystem.dto.user.editpassword.UserPasswordInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.exception.invalidvalue.InvalidNewPasswordException;
import com.test.studyroomreservationsystem.exception.invalidvalue.InvalidCurrentPasswordException;
import com.test.studyroomreservationsystem.exception.user.EmailAlreadyExistsException;
import com.test.studyroomreservationsystem.exception.user.SerialAlreadyExistsException;
import com.test.studyroomreservationsystem.service.UserService;
import com.test.studyroomreservationsystem.exception.user.UsernameAlreadyExistsException;
import com.test.studyroomreservationsystem.exception.notfound.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }
    /*-------------------------------------------------*/
    @Override
    public User signUpProcess(SingUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String serial = requestDto.getSerial();
        String email = requestDto.getEmail();

        // 해당 로그인 ID 가 이미 존재하는 아이디인지?
        boolean existsByUsername = userRepository.existsByUsername(username);
        if (existsByUsername) {
            throw (new UsernameAlreadyExistsException(username));
        }
        // 해당 학번이 이미 존재 하는지?
        boolean existsBySerial = userRepository.existsBySerial(serial);
        if (existsBySerial) {
            throw (new SerialAlreadyExistsException(serial));
        }
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            throw (new EmailAlreadyExistsException(email));
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setSerial(serial);
        user.setName(requestDto.getName());
        user.setIsAdmin(Boolean.FALSE);
        user.setEmail(email);
        return userRepository.save(user);
    }


    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
    @Override
    public User findBySerial(String serial) {
        return userRepository.findBySerial(serial)
                .orElseThrow(() -> new UserNotFoundException(serial));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserInfo(Long userId, UserInfoUpdateRequestDto requestDto) {
        User user = findUserById(userId);
        requestDto.toEntity(user);
        return userRepository.save(user);
    }


    @Override
    public User resetUserPassword(Long userId, UserPasswordInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = findUserById(userId);
        String currentPassword = userInfoUpdateRequestDto.getPrePassword();

        // 기존 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }
        // 새 비밀번호 암호화 및 업데이트
        String newPassword = userInfoUpdateRequestDto.getNewPassword();
        if (bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidNewPasswordException();
        }
        String encodeNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodeNewPassword);

        return userRepository.save(user);
    }

    @Override
    public User resetUserPassword(Long userId, UserPasswordInfoResetRequestDto resetRequestDto) {
        User user = findUserById(userId);
        // 새 비밀번호 암호화 및 업데이트
        String newPassword = resetRequestDto.getNewPassword();
        if (bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidNewPasswordException();
        }
        String encodeNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodeNewPassword);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        findUserById(userId); //찾아보고 없으면 예외처리
        userRepository.deleteById(userId);
    }

    
}
