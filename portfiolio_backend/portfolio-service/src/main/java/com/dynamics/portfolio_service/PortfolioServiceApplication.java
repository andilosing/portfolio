package com.dynamics.portfolio_service;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients
public class PortfolioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioServiceApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set the default timezone to Europe/Berlin
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		System.out.println("Default timezone set to Europe/Berlin");
	}

}
