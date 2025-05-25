package com.example.gestionequipe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient

public class GestionEquipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEquipeApplication.class, args);
	}

}
