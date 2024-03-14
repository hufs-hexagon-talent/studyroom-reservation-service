package com.test.studyroomreservationsystem.dto;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UserUpdateDto { // U dto
    private String loginId;
    private String password;
    private String serial;
    private Boolean isAdmin;
}
