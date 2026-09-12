package com.example.IoT.reservation.repository;

import com.example.IoT.reservation.domain.Reservation;
import com.example.IoT.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberId(String memberId);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByRoomIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeAsc(
            Long roomId, Collection<ReservationStatus> statuses,
            LocalDateTime endTime, LocalDateTime startTime);

    boolean existsByRoomIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, Collection<ReservationStatus> statuses,
            LocalDateTime endTime, LocalDateTime startTime);

    boolean existsByRoomIdAndIdNotAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, Long excludeId, Collection<ReservationStatus> statuses,
            LocalDateTime endTime, LocalDateTime startTime);
}
