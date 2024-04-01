package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import com.test.studyroomreservationsystem.dto.user.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;
import com.test.studyroomreservationsystem.service.exception.LoginIdAlreadyExistsException;
import com.test.studyroomreservationsystem.service.exception.SerialAlreadyExistsException;
import com.test.studyroomreservationsystem.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*-------------------------------------------------*/
    @Override
    public User createUser(UserDto userDto) {

        isUserAlreadyExists(userDto);
        // 유효성 검사하기 , 비밀번호 암호화 하기

        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(userDto.getPassword());
        user.setSerial(userDto.getSerial());
        user.setIsAdmin(userDto.getIsAdmin());
        user.setUserName(userDto.getUserName());

        return userRepository.save(user);
    }

    private void isUserAlreadyExists(UserDto userDto) {
        userRepository.findByLoginId(userDto.getLoginId()).ifPresent(user -> {
            throw new LoginIdAlreadyExistsException(userDto.getLoginId());
            }
        );
        userRepository.findBySerial(userDto.getSerial()).ifPresent(user -> {
            throw new SerialAlreadyExistsException(userDto.getSerial());
            }
        );
    }

    @Override
    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = findUserById(userId);

        user.setLoginId(userUpdateDto.getLoginId());
        user.setPassword(userUpdateDto.getPassword());
        user.setSerial(userUpdateDto.getSerial());
        user.setIsAdmin(userUpdateDto.getIsAdmin());
        user.setUserName(userUpdateDto.getUserName());


        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLoginId(user.getLoginId());
        userDto.setPassword(user.getPassword());
        userDto.setSerial(user.getSerial());
        userDto.setIsAdmin(user.getIsAdmin());
        return userDto;
    }
}
