package com.example.IoT.reservation.repository;

import com.example.IoT.reservation.domain.Reservation;
import com.example.IoT.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberId(String memberId);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByRoomIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeAsc(
            Long roomId, ReservationStatus excludeStatus,
            LocalDateTime endTime, LocalDateTime startTime);

    boolean existsByRoomIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, ReservationStatus excludeStatus,
            LocalDateTime endTime, LocalDateTime startTime);

    boolean existsByRoomIdAndIdNotAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, Long excludeId, ReservationStatus excludeStatus,
            LocalDateTime endTime, LocalDateTime startTime);
}
