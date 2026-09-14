package com.example.IoT.aduino.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Aduino {
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

    public Aduino(LocalDateTime openAt) {
        this.openAt = openAt;
        this.status = ReservationStatus.RESERVED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }
}
