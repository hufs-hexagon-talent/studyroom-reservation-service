package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;
import lombok.Builder;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);
    User findUserById(Long userId);
    List<User> findAllUsers();
    User updateUser(Long userId, UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
    default UserDto toDto(User user) {
        return UserDto.builder()
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .serial(user.getSerial())
                .isAdmin(user.getIsAdmin())
                .userName(user.getUserName())
                .build();
    }
}
