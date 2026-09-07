package com.example.IoT.aduino.DTO;

public record CommandResponse(
        int status,
        String command
) {
}
