package com.example.IoT.aduino.repository;

import com.example.IoT.aduino.domain.Aduino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AduinoRepository extends JpaRepository<Aduino, Long> {
}
