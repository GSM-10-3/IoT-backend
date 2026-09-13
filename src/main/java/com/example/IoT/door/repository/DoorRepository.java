package com.example.IoT.door.repository;

import com.example.IoT.door.domain.Door;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoorRepository extends JpaRepository<Door, Long> {

    List<Door> findAllByOrderByIdAsc();

    List<Door> findByActiveTrueOrderByIdAsc();
}
