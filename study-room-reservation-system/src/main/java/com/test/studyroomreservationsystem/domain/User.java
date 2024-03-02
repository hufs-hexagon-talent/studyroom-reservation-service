package com.test.studyroomreservationsystem.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "User")
public class User {

    @Id
    private Long userId;
    private String name;
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(Long userId, String name, String password, boolean isAdmin) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

}
