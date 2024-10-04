package com.example.OverclockHeaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class OverclockHeavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(OverclockHeavenApplication.class, args);
	}

}
