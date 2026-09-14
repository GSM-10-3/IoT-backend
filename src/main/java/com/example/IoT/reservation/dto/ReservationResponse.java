package com.example.IoT.reservation.dto;

import com.example.IoT.reservation.domain.Reservation;
import com.example.IoT.reservation.domain.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        String memberId,
        Long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status
) {
    public static ReservationResponse from(Reservation r) {
        return new ReservationResponse(
                r.getId(), r.getMemberId(), r.getRoomId(),
                r.getStartTime(), r.getEndTime(), r.getStatus()
        );
    }
}