package com.dynamics.users;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set the default timezone to Europe/Berlin
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		System.out.println("Default timezone set to Europe/Berlin");
	}

}
