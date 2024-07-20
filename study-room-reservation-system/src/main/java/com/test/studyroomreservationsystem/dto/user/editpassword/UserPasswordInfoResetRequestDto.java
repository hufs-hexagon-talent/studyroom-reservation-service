package com.test.studyroomreservationsystem.dto.user.editpassword;


import lombok.Getter;

public class UserPasswordInfoResetRequestDto implements UserPasswordInfoUpdateRequest{
    @Getter
    String token;
    String prePassword;
    String newPassword;

    public UserPasswordInfoResetRequestDto(String token,
                                           String prePassword,
                                           String newPassword) {
        this.token = token;
        this.prePassword = prePassword;
        this.newPassword = newPassword;
    }

    @Override
    public String getPrePassword() {
        return prePassword;
    }

    @Override
    public String getNewPassword() {
        return newPassword;
    }
}
