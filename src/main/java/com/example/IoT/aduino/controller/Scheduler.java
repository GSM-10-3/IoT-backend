package com.example.IoT.aduino.controller;

import com.example.IoT.aduino.service.AduinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final AduinoService aduinoService;
}
