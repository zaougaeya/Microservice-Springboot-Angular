package com.example.gestionmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = { SecurityAutoConfiguration.class })
public class  GestionMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionMatchApplication.class, args);
	}

}