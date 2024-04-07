package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.UserDao;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;
import com.test.studyroomreservationsystem.service.UserService;
import com.test.studyroomreservationsystem.service.exception.LoginIdAlreadyExistsException;
import com.test.studyroomreservationsystem.service.exception.SerialAlreadyExistsException;
import com.test.studyroomreservationsystem.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    /*-------------------------------------------------*/
    @Override
    public User createUser(UserDto userDto) {

        isUserAlreadyExists(userDto);
        // todo  유효성 검사하기 , 비밀번호 암호화 하기

        User userEntity = userDto.toEntity();

        return userDao.save(userEntity);
    }

    private void isUserAlreadyExists(UserDto userDto) {
        userDao.findByLoginId(userDto.getLoginId()).ifPresent(user -> {
            throw new LoginIdAlreadyExistsException(userDto.getLoginId());
            }
        );
        userDao.findBySerial(userDto.getSerial()).ifPresent(user -> {
            throw new SerialAlreadyExistsException(userDto.getSerial());
            }
        );
    }

    @Override
    public User findUserById(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }


    @Override
    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User userEntity = findUserById(userId);

        userEntity.setLoginId(userUpdateDto.getLoginId());
        userEntity.setPassword(userUpdateDto.getPassword());
        userEntity.setSerial(userUpdateDto.getSerial());
        userEntity.setIsAdmin(userUpdateDto.getIsAdmin());
        userEntity.setUserName(userUpdateDto.getUserName());

        return userDao.save(userEntity);
    }
    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById(userId);
    }

    
}
