package com.example.IoT.reservation.service;

import com.example.IoT.door.domain.Door;
import com.example.IoT.door.repository.DoorRepository;
import com.example.IoT.reservation.domain.Reservation;
import com.example.IoT.reservation.domain.ReservationStatus;
import com.example.IoT.reservation.dto.ReservationCreateRequest;
import com.example.IoT.reservation.dto.ReservationResponse;
import com.example.IoT.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DoorRepository doorRepository;

    @Transactional
    public ReservationResponse add(ReservationCreateRequest request) {
        validateDoor(request.roomId());
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
        validateOverlap(reservation.getRoomId(), id, start, end);
        reservation.changeTime(start, end);

        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> findTimetable(Long roomId, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);

        return reservationRepository
                .findByRoomIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeAsc(
                        roomId, ReservationStatus.CANCELED, dayEnd, dayStart)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findPending() {
        return reservationRepository.findByStatus(ReservationStatus.PENDING)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse approve(Long id) {
        Reservation reservation = findOrThrow(id);
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 예약만 처리할 수 있습니다.");
        }
        reservation.approve();
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse refuse(Long id) {
        Reservation reservation = findOrThrow(id);
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 예약만 처리할 수 있습니다.");
        }
        reservation.refuse();
        return ReservationResponse.from(reservation);
    }

    private Reservation findOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + id));
    }

    private void validateDoor(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("문 id는 필수입니다.");
        }
        Door door = doorRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("문을 찾을 수 없습니다. id=" + roomId));
        if (!door.isActive()) {
            throw new IllegalStateException("사용할 수 없는 문입니다. id=" + roomId);
        }
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

    private void validateOverlap(Long roomId, Long excludeId, LocalDateTime start, LocalDateTime end) {
        boolean overlapped = reservationRepository
                .existsByRoomIdAndIdNotAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
                        roomId, excludeId, ReservationStatus.CANCELED, end, start);
        if (overlapped) {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }
    }
}
