package com.test.studyroomreservationsystem.dto.user;


import lombok.Getter;

@Getter
public class UserDto { // CR dto
    private String loginId;
    private String password;
    private String serial;
    private Boolean isAdmin;
    private String userName;


// of :  Dto -> Entity
// from :  Entity -> Dto

//    public UserDto of(User user) {
//
//        return
//    }
//
//    public User from(UserDto userDto) {
//        return
//    }
}
