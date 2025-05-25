package com.example.gestionuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.example.userapi.client")
@SpringBootApplication
public class GestionUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionUserApplication.class, args);
	}

}
