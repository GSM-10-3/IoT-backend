package com.example.IoT.auth.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("STUDENT", "학생"),
    TEACHER("TEACHER", "선생님"),
    ADMIN("ADMIN", "관리자");

    private final String role;
    private final String description;

    UserRole(String role, String description) {
        this.role = role;
        this.description = description;
    }
}
