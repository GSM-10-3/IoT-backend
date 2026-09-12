package com.example.IoT.common.exception;

public record ErrorResponse(
        int status,
        String message
) {}
