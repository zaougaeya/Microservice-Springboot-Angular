package com.example.testclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.userapi.client")

public class TestClientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestClientServiceApplication.class, args);
    }
}