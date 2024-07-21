package com.test.studyroomreservationsystem.dto.user.editpassword;


import lombok.Getter;

@Getter
public class UserPasswordInfoResetRequestDto {

    String token;
    String newPassword;

    public UserPasswordInfoResetRequestDto(String token,
                                           String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }
}
