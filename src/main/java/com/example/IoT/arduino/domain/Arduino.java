package com.example.IoT.arduino.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Arduino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private LocalDateTime openAt;

    @Getter
    @Setter
    @Column(name = "status")
    private ReservationStatus status;

    public Arduino(LocalDateTime openAt) {
        this.openAt = openAt;
        this.status = ReservationStatus.RESERVED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }
}
