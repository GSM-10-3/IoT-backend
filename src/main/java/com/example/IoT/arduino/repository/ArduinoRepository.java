package com.example.IoT.arduino.repository;

import com.example.IoT.arduino.domain.Arduino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArduinoRepository extends JpaRepository<Arduino, Long> {
}
