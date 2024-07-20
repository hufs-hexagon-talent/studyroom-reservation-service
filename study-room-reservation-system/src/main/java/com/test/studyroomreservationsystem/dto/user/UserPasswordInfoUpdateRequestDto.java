package com.test.studyroomreservationsystem.dto.user;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserPasswordInfoUpdateRequestDto implements UserPasswordInfoUpdateRequest { // U dto
    private String prePassword;
    private String newPassword;

    @Override
    public String getPrePassword() {
        return prePassword;
    }

    @Override
    public String getNewPassword() {
        return newPassword;
    }
}
