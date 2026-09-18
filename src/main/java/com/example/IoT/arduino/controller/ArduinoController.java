package com.example.IoT.arduino.controller;

import com.example.IoT.arduino.DTO.Command;
import com.example.IoT.arduino.DTO.CommandResponse;
import com.example.IoT.arduino.DTO.ResultResponse;
import com.example.IoT.arduino.service.ArduinoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/door")
public class ArduinoController {
    private final ArduinoService arduinoService;

    public ArduinoController(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    @PostMapping("/{id}/command")
    public ResponseEntity<?> command(@PathVariable Long id,
                                     @RequestBody Command command) {
        try {
            if (command.command()) {
                return ResponseEntity.ok(new CommandResponse(200, "open"));
            } else {
                return ResponseEntity.ok(new CommandResponse(200, "close"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommandResponse(400, "잘못된 요청입니다."));
        }
    }

    @PostMapping("/{id}/result")
    public ResponseEntity<?> result(@PathVariable Long id) {
        try {

            return ResponseEntity.ok(new ResultResponse(200, "성공적으로 실행되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResultResponse(400, "잘못된 요청입니다."));
        }
    }
}
