package com.cinemasystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CinemaManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaManagementSystemApplication.class, args);
	}

}
