package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.security.dto.SingUpRequestDto;
import com.test.studyroomreservationsystem.security.dto.UserInfoResponseDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;


import java.util.List;

public interface UserService {
    User signUpProcess(SingUpRequestDto requestDto);
    User findUserById(Long userId);
    User findByUsername(String username);
    List<User> findAllUsers();
    User updateUser(Long userId, UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
    default UserInfoResponseDto dtoFrom(User user) {
        return UserInfoResponseDto.builder()
                .username(user.getUsername())
                .serial(user.getSerial())
                .isAdmin(user.getIsAdmin())
                .name(user.getName())
                .build();
    }


}
