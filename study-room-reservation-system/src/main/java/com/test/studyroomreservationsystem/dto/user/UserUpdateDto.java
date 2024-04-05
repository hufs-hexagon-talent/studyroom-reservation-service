package com.test.studyroomreservationsystem.dto.user;


import lombok.Getter;

@Getter
public class UserUpdateDto { // U dto
    private String loginId;
    private String password;
    private String serial;
    private Boolean isAdmin;
    private String userName;
}
