package com.example.IoT.reservation.repository;

import com.example.IoT.reservation.domain.Reservation;
import com.example.IoT.reservation.domain.ReservationStatus;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberId(String memberId);

    List<Reservation> findByStatus(ReservationStatus status);

    boolean existsByRoomIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, ReservationStatus excludeStatus,
            LocalDateTime endTime, LocalDateTime startTime);
}
