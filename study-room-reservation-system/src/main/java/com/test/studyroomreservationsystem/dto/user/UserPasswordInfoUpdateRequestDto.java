package com.test.studyroomreservationsystem.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordInfoUpdateRequestDto { // U dto
    private String prePassword;
    private String newPassword;

}
