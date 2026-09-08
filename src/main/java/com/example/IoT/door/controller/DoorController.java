package com.example.IoT.door.controller;

import com.example.IoT.door.dto.DoorCreateRequest;
import com.example.IoT.door.dto.DoorResponse;
import com.example.IoT.door.service.DoorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/door")
@RequiredArgsConstructor
public class DoorController {

    private final DoorService doorService;

    @GetMapping
    public ResponseEntity<List<DoorResponse>> list() {
        return ResponseEntity.ok(doorService.findActive());
    }

    @PostMapping("/add")
    public ResponseEntity<DoorResponse> add(@RequestBody DoorCreateRequest request) {
        return ResponseEntity.ok(doorService.add(request));
    }
}
