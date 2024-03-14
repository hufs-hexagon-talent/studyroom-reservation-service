package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String loginId;

    private String password;

    @Column(unique = true)
    private String serial;

    private Boolean isAdmin;

}
