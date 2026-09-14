package com.example.IoT.reservation.controller;

import com.example.IoT.reservation.dto.ReservationDecisionRequest;
import com.example.IoT.reservation.dto.ReservationResponse;
import com.example.IoT.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationResponse>> pendingList() {
        return ResponseEntity.ok(reservationService.findPending());
    }

    @PostMapping("/reservation-approve")
    public ResponseEntity<ReservationResponse> approve(
            @RequestBody ReservationDecisionRequest request) {
        return ResponseEntity.ok(reservationService.approve(request.reservationId()));
    }

    @PostMapping("/reservation-refusal")
    public ResponseEntity<ReservationResponse> refuse(
            @RequestBody ReservationDecisionRequest request) {
        return ResponseEntity.ok(reservationService.refuse(request.reservationId()));
    }
}