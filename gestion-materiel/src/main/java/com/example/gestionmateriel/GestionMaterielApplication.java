package com.example.gestionmateriel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.userapi.client")
public class GestionMaterielApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionMaterielApplication.class, args);
    }



}
