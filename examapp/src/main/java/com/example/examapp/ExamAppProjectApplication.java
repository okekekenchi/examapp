package com.example.examapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExamAppProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamAppProjectApplication.class, args);
	}

}
