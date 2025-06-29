package com.example.gestionproduit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.userapi.client")
@EnableDiscoveryClient
@ComponentScan(basePackages = {
    "com.example.gestionproduit",
    "com.example.userapi" // <--- ajoute ça
})

public class GestionProduitApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionProduitApplication.class, args);
    }

}
