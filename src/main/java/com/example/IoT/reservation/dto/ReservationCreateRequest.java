package com.example.IoT.reservation.dto;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        String memberId,
        Long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}