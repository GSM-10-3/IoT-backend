package com.example.IoT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.awt.image.DataBuffer;

@SpringBootApplication
@EnableScheduling
public class IoTApplication {

	public static void main(String[] args) {
		SpringApplication.run(IoTApplication.class, args);
	}

}
