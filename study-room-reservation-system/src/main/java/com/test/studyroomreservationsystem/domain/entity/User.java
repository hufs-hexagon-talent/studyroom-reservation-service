package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "login_id", unique = true)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name="serial", unique = true, length = 9)
    private String serial;

    @Column(name="user_name")
    private String userName;

    @Column(name="is_admin")
    private Boolean isAdmin = false; // 기본값



    @Builder
    public User(Long userId, String loginId, String password, String serial, Boolean isAdmin, String userName) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.serial = serial;
        this.isAdmin = isAdmin;
        this.userName = userName;
    }
}
