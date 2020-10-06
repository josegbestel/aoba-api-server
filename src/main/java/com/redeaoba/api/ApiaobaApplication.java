package com.redeaoba.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiaobaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiaobaApplication.class, args);
	}

}
