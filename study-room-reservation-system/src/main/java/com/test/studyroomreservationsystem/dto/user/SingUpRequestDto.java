package com.test.studyroomreservationsystem.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
public class SingUpRequestDto {
    private String username;
    private String password;
    private String serial;
    private String name;


    public SingUpRequestDto(String username, String password, String serial, String name) {
        this.username = username;
        this.password = password;
        this.serial = serial;
        this.name = name;

    }
// todo : 어차피 대부분의 회원가입 유저는 어드민이 아니기에 업데이트 방식으로 어드민 지정
    public SingUpRequestDto toEntity() {
        return SingUpRequestDto.builder()
                .username(username)
                .password(password)
                .serial(serial)
                .name(name)
                .build();
    }
}
