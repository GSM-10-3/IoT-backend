package com.example.IoT.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "email_verify")
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false, length = 160)
    private String email;

    @Getter
    @Column(nullable = false, length = 6)
    private String code;

    @Getter
    @Column(nullable = false)
    private boolean verified = false;

    @Getter
    @Setter
    private boolean used = false;

    @Getter
    @Column(nullable = false)
    private Instant expiresAt;

    @Getter
    @Column(nullable = false)
    private Instant createdAt;

    protected EmailVerification() {
    }

    public EmailVerification(String email,
                             String code,
                             Instant expiresAt,
                             Instant createdAt) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public void verify() {
        this.verified = true;
    }
    public void used(){
        this.used = true;
    }

}
