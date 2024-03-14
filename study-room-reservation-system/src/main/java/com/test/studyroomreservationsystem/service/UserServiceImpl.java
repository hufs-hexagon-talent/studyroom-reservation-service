package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import com.test.studyroomreservationsystem.dto.UserDto;
import com.test.studyroomreservationsystem.dto.UserUpdateDto;
import com.test.studyroomreservationsystem.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        // 유효성 검사하기 , 비밀번호 암호화 하기
        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(userDto.getPassword());
        user.setSerial(userDto.getSerial());
        user.setIsAdmin(userDto.getIsAdmin());
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User findedUser = findUserById(userId);

        findedUser.setLoginId(userUpdateDto.getLoginId());
        findedUser.setPassword(userUpdateDto.getPassword());
        findedUser.setSerial(userUpdateDto.getSerial());
        findedUser.setIsAdmin(userUpdateDto.getIsAdmin());

        return userRepository.save(findedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
