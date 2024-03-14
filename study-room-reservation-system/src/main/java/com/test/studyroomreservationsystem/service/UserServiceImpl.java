package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import com.test.studyroomreservationsystem.dto.UserDto;
import com.test.studyroomreservationsystem.dto.UserUpdateDto;
import com.test.studyroomreservationsystem.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*-------------------------------------------------*/
    @Override
    public UserDto createUser(UserDto userDto) {
        // 유효성 검사하기 , 비밀번호 암호화 하기
        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(userDto.getPassword());
        user.setSerial(userDto.getSerial());
        user.setIsAdmin(userDto.getIsAdmin());
        return convertToDto(userRepository.save(user));
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return convertToDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setLoginId(userUpdateDto.getLoginId());
        user.setPassword(userUpdateDto.getPassword());
        user.setSerial(userUpdateDto.getSerial());
        user.setIsAdmin(userUpdateDto.getIsAdmin());

        return convertToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLoginId(user.getLoginId());
        userDto.setPassword(user.getPassword());
        userDto.setSerial(user.getSerial());
        userDto.setIsAdmin(user.getIsAdmin());
        return userDto;
    }
}
