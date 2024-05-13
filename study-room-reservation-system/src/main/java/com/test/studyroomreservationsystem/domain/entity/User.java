package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", unique = true)
    private String username; //loginId

    @Column(name = "password")
    private String password;

    @Column(name="serial", unique = true, length = 9)
    private String serial;

    @Column(name="name")
    private String name; // 진짜 이름

    @Column(name="is_admin")
    private Boolean isAdmin = false; // 기본값


    @Builder
    public User(Long userId, String username, String password, String serial, Boolean isAdmin, String name) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.serial = serial;
        this.isAdmin = isAdmin != null ? isAdmin : false; // null 체크 후 기본값 할당
        this.name = name;
    }
    public Boolean getIsAdmin() {
        return isAdmin != null && isAdmin; // null을 안전하게 처리
    }

}
