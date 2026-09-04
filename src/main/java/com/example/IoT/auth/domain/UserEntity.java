package com.example.IoT.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "userss")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    public UserEntity(String username, String email,String password, UserRole role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
