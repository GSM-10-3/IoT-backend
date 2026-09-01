package com.example.IoT.reservation.service;

import com.example.IoT.reservation.domain.Reservation;
import com.example.IoT.reservation.domain.ReservationStatus;
import com.example.IoT.reservation.dto.ReservationCreateRequest;
import com.example.IoT.reservation.dto.ReservationResponse;
import com.example.IoT.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse add(ReservationCreateRequest request) {
        validateTime(request.startTime(), request.endTime());
        validateOverlap(request.roomId(), request.startTime(), request.endTime());

        Reservation reservation = Reservation.builder()
                .memberId(request.memberId())
                .roomId(request.roomId())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> findByMember(String memberId) {
        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void cancel(Long id) {
        findOrThrow(id).cancel();
    }

    @Transactional
    public ReservationResponse change(Long id, LocalDateTime start, LocalDateTime end) {
        validateTime(start, end);

        Reservation reservation = findOrThrow(id);
        validateOverlap(reservation.getRoomId(), start, end);
        reservation.changeTime(start, end);

        return ReservationResponse.from(reservation);
    }

    private Reservation findOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + id));
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }
    }

    private void validateOverlap(Long roomId, LocalDateTime start, LocalDateTime end) {
        boolean overlapped = reservationRepository
                .existsByRoomIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
                        roomId, ReservationStatus.CANCELED, end, start);
        if (overlapped) {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }
    }
}