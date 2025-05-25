package com.example.gestionequipe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.example.userapi.client")
public class GestionEquipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEquipeApplication.class, args);
	}

}
