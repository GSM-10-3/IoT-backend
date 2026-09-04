package com.example.IoT.auth.DTO;

public record SignupRequest(String username,
                            String email,
                            String password) {
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
