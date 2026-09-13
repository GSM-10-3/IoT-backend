package com.example.IoT.reservation.controller;

import com.example.IoT.reservation.dto.ReservationChangeRequest;
import com.example.IoT.reservation.dto.ReservationCreateRequest;
import com.example.IoT.reservation.dto.ReservationResponse;
import com.example.IoT.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<ReservationResponse> add(
            @RequestBody ReservationCreateRequest request) {
        return ResponseEntity.ok(reservationService.add(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> list(@RequestParam String memberId) {
        return ResponseEntity.ok(reservationService.findByMember(memberId));
    }

    @DeleteMapping("/cancle")
    public ResponseEntity<Void> cancel(@RequestParam Long reservationId) {
        reservationService.cancel(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change")
    public ResponseEntity<ReservationResponse> change(
            @RequestBody ReservationChangeRequest request) {
        return ResponseEntity.ok(reservationService.change(
                request.reservationId(), request.startTime(), request.endTime()));
    }
}
