package com.test.studyroomreservationsystem.dto.user;


import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class UserDto { // CR dto
    private String loginId;
    private String password;
    private String serial;
    private Boolean isAdmin;
}
