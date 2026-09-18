package com.example.IoT.arduino.controller;

import com.example.IoT.arduino.service.ArduinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ArduinoService arduinoService;
}
