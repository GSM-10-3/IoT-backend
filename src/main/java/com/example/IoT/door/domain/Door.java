package com.example.IoT.door.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Door {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime createdAt;

    @Builder
    private Door(String name, String location) {
        this.name = name;
        this.location = location;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }
}
