package com.example.IoT.door.service;

import com.example.IoT.door.domain.Door;
import com.example.IoT.door.dto.DoorCreateRequest;
import com.example.IoT.door.dto.DoorResponse;
import com.example.IoT.door.repository.DoorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoorService {

    private final DoorRepository doorRepository;

    @Transactional
    public DoorResponse add(DoorCreateRequest request) {
        validateName(request.name());

        Door door = Door.builder()
                .name(request.name())
                .location(request.location())
                .build();

        return DoorResponse.from(doorRepository.save(door));
    }

    public List<DoorResponse> findAll() {
        return doorRepository.findAllByOrderByIdAsc()
                .stream()
                .map(DoorResponse::from)
                .toList();
    }

    public List<DoorResponse> findActive() {
        return doorRepository.findByActiveTrueOrderByIdAsc()
                .stream()
                .map(DoorResponse::from)
                .toList();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("문 이름은 필수입니다.");
        }
    }
}
