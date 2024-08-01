package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true)
    private String username; //loginId

    @Column(name = "password")
    private String password;

    @Column(name="serial", unique = true)
    private String serial;

    @Column(name="email")
    private String email;

    @Column(name="name")
    private String name; // 진짜 이름

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('USER', 'ADMIN', 'RESIDENT') NOT NULL DEFAULT 'USER'")
    private ServiceRole serviceRole; // 기본값 USER


    @Builder
    public User(Long userId,
                String username,
                String password,
                String serial,
                String email,
                ServiceRole serviceRole,
                String name) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.serial = serial;
        this.email = email;
        this.serviceRole = serviceRole != null ? serviceRole : ServiceRole.USER; // null 체크 후 기본값 할당
        this.name = name;
    }
    public enum ServiceRole {
        USER, ADMIN, RESIDENT
    }
}
