package com.example.IoT.arduino.service;

import com.example.IoT.arduino.repository.ArduinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArduinoService {
    private final ArduinoRepository arduinoRepository;

    public void open(){

    }

    public void result(){

    }

    public void checkReservation(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

}
