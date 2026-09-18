package com.example.IoT.arduino.DTO;

public record CommandResponse(
        int status,
        String command
) {
}
