package com.example.IoT.auth.DTO;

public record LoginRequest(String username,
                           String email,
                           String password) {
}
