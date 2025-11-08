package com.exileaccused;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.exileaccused")
public class ExileAccusedApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExileAccusedApplication.class, args);
	}

}
