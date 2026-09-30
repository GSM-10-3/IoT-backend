package com.example.IoT.arduino.repository;

import com.example.IoT.arduino.domain.Arduino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArduinoRepository extends JpaRepository<Arduino, Long> {
    List<Arduino> findByStatusTrueAndOpenAtLessThanEqual(LocalDateTime time);
}
