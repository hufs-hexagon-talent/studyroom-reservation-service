package com.test.studyroomreservationsystem.security.service;

import com.test.studyroomreservationsystem.dao.UserDao;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.security.dto.SingUpRequestDto;
import com.test.studyroomreservationsystem.service.UserService;
import com.test.studyroomreservationsystem.service.exception.UsernameAlreadyExistsException;
import com.test.studyroomreservationsystem.service.exception.UserNotFoundException;
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
        log.trace("\n[회원가입]유저네임 : {}",username);
        log.trace("\n[회원가입]비밀번호 : {}",password);
        // todo :  다른 값들도 받아서 세팅

        Boolean isExist = userDao.existsByUsername(username);

        if (isExist) {
            throw (new UsernameAlreadyExistsException(username));
        }
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setSerial(requestDto.getSerial());
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
        User userEntity = findUserById(userId);

        userEntity.setUsername(userInfoUpdateRequestDto.getUsername());
        userEntity.setPassword(userInfoUpdateRequestDto.getPassword());
        userEntity.setSerial(userInfoUpdateRequestDto.getSerial());
        userEntity.setIsAdmin(userInfoUpdateRequestDto.getIsAdmin());
        userEntity.setName(userInfoUpdateRequestDto.getName());

        return userDao.save(userEntity);
    }
    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById(userId);
    }

    
}
