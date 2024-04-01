package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "login_id", unique = true)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name="serial", unique = true)
    private String serial;

    @Column(name="is_admin")
    private Boolean isAdmin;

    @Column(name="user_name")
    private String userName;



}
