package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.UserDao;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.dto.user.SingUpRequestDto;
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

    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    /*-------------------------------------------------*/
    @Override
    public User signUpProcess(SingUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String serial = requestDto.getSerial();

        // 해당 로그인 ID 가 이미 존재하는 아이디인지?
        boolean existsByUsername = userDao.existsByUsername(username);
        if (existsByUsername) {
            throw (new UsernameAlreadyExistsException(username));
        }
        // 해당 학번이 이미 존재 하는지?
        boolean existsBySerial = userDao.existsBySerial(serial);
        if (existsBySerial) {
            throw (new SerialAlreadyExistsException(serial));
        }


        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setSerial(serial);
        user.setName(requestDto.getName());
        user.setIsAdmin(Boolean.FALSE);

        return userDao.save(user);
    }


    @Override
    public User findUserById(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }


    @Override
    public User updateUser(Long userId, UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = findUserById(userId);

        String newPassword = userInfoUpdateRequestDto.getPassword();
        String encodeNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodeNewPassword);

        return userDao.save(user);
    }
    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById(userId);
    }

    
}
