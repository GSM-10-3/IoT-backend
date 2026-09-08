package com.example.IoT.reservation.dto;

import java.time.LocalDateTime;

public record ReservationChangeRequest(
        Long reservationId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
