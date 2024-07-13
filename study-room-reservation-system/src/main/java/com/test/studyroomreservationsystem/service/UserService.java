package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.SingUpRequestDto;
import com.test.studyroomreservationsystem.dto.user.UserInfoResponseDto;
import com.test.studyroomreservationsystem.dto.user.UserInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.dto.user.UserPasswordInfoUpdateRequestDto;


import java.util.List;

public interface UserService {
    User signUpProcess(SingUpRequestDto requestDto);
    User findUserById(Long userId);
    User findByUsername(String username);
    String findEmailByUsername(String username);
    List<User> findAllUsers();
    User updateUserInfo(Long userId,UserInfoUpdateRequestDto requestDto);
    User updateUserPassword(Long userId, UserPasswordInfoUpdateRequestDto userInfoUpdateRequestDto);
    void deleteUser(Long userId);
    default UserInfoResponseDto dtoFrom(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .serial(user.getSerial())
                .isAdmin(user.getIsAdmin())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


}
