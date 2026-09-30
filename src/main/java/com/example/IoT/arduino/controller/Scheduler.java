package com.example.IoT.arduino.controller;

import com.example.IoT.arduino.service.ArduinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final ArduinoService arduinoService;

    @Scheduled(fixedRate = 1000)
    public void checkReservation(){

    }
}
